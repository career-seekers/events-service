package org.careerseekers.cseventsservice.services

import org.careerseekers.cseventsservice.cache.UsersCacheClient
import org.careerseekers.cseventsservice.dto.DirectionCreation
import org.careerseekers.cseventsservice.dto.directions.CreateDirectionDto
import org.careerseekers.cseventsservice.dto.directions.UpdateDirectionDto
import org.careerseekers.cseventsservice.dto.directions.categories.CreateAgeCategory
import org.careerseekers.cseventsservice.entities.ChildToDirection
import org.careerseekers.cseventsservice.entities.DirectionAgeCategories
import org.careerseekers.cseventsservice.entities.Directions
import org.careerseekers.cseventsservice.enums.DirectionAgeCategory
import org.careerseekers.cseventsservice.enums.QueueStatus
import org.careerseekers.cseventsservice.exceptions.NotFoundException
import org.careerseekers.cseventsservice.mappers.DirectionsMapper
import org.careerseekers.cseventsservice.repositories.ChildToDirectionRepository
import org.careerseekers.cseventsservice.repositories.DirectionsRepository
import org.careerseekers.cseventsservice.services.interfaces.CrudService
import org.careerseekers.cseventsservice.services.kafka.producers.DirectionCreationKafkaProducer
import org.careerseekers.cseventsservice.utils.DocumentsApiResolver
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import kotlin.also

@Service
class DirectionsService(
    override val repository: DirectionsRepository,
    private val childToDirectionRepository: ChildToDirectionRepository,
    private val directionsMapper: DirectionsMapper,
    private val documentsApiResolver: DocumentsApiResolver,
    private val usersCacheClient: UsersCacheClient,
    private val directionCreationKafkaProducer: DirectionCreationKafkaProducer,
    private val directionAgeCategoriesService: DirectionAgeCategoriesService,
) : CrudService<Directions, Long, CreateDirectionDto, UpdateDirectionDto> {

    fun getByUserId(userId: Long): List<Directions> = repository.findByUserId(userId)

    fun getByExpertId(expertId: Long): List<Directions> = repository.findByExpertId(expertId)

    fun getByAgeCategory(ageCategory: DirectionAgeCategory): List<Directions> =
        repository.findByAgeCategory(ageCategory)

    @Transactional
    override fun create(item: CreateDirectionDto): Directions {
        val tutor = item.userId?.let {
            usersCacheClient.getItemFromCache(it) ?: throw NotFoundException("Пользователь с ID $it не найден.")
        }
        val expert = item.expertId?.let {
            usersCacheClient.getItemFromCache(it) ?: throw NotFoundException("Пользователь с ID $it не найден.")
        }

        return repository.save(
            directionsMapper.directionFromDto(
                item.copy(
                    iconId = item.icon?.let { documentsApiResolver.loadDocId("uploadDirectionIcon", it) }
                ))
        ).let { direction ->
            val categories = mutableListOf<DirectionAgeCategories>()
            item.ageCategory.forEach { ageCategory ->
                categories.add(
                    directionAgeCategoriesService.create(
                        CreateAgeCategory(
                            ageCategory = ageCategory,
                            direction = direction
                        )
                    )
                )
            }

            direction.ageCategories = categories
            repository.save(direction)
        }.also { direction ->
            directionCreationKafkaProducer.sendMessage(
                DirectionCreation(
                    name = direction.name,
                    tutor = tutor!!,
                    expert = expert!!
                )
            )
        }
    }

    @Transactional
    override fun createAll(items: List<CreateDirectionDto>): String {
        for (item in items) {
            create(item)
        }

        return "Все компетенции созданы успешно."
    }

    @Transactional
    override fun update(item: UpdateDirectionDto): String {
        getById(item.id, message = "Компетенция с ID '${item.id}' не найдена.")!!.apply {
            item.name?.let { name = it }
            item.description?.let { description = it }
            item.ageCategory?.let {
                directionAgeCategoriesService.getAll()
                    .filter { category -> category.direction == this }
                    .forEach { category -> directionAgeCategoriesService.deleteById(category.id) }

                ageCategories?.clear()

                item.ageCategory.forEach { category ->
                    ageCategories?.add(
                        directionAgeCategoriesService.create(
                            CreateAgeCategory(
                                ageCategory = category,
                                direction = this
                            )
                        )
                    )
                }
            }
            item.icon?.let {
                val oldIconId = this.iconId

                iconId = documentsApiResolver.loadDocId("uploadDirectionIcon", it)
                oldIconId?.let { iconId -> documentsApiResolver.deleteDocument(iconId) }
            }
            item.expertId?.let {
                usersCacheClient.getItemFromCache(it) ?: throw NotFoundException("Пользователь с ID $it не найден.")
                expertId = it
            }
        }.also(repository::save)

        return "Данные компетенции обновлены успешно."
    }

    @Transactional
    fun updateDirectionParticipants(direction: Directions) {
        val records = childToDirectionRepository.findByDirectionId(direction.id)
            .sortedBy { it.createdAt }

        val activeCountsByCategory = records
            .filter { it.queueStatus == QueueStatus.PARTICIPATES }
            .groupingBy { it.directionAgeCategory.id }
            .eachCount()
            .mapValues { it.value.toLong() }
            .toMutableMap()

        val modifiedChildren = mutableListOf<ChildToDirection>()
        for (child in records) {
            val catId = child.directionAgeCategory.id
            val activeCount = activeCountsByCategory.getOrDefault(catId, 0L)
            val maxCount = child.directionAgeCategory.maxParticipantsCount

            var statusChanged = false
            if (child.queueStatus == QueueStatus.IN_QUEUE && activeCount < maxCount && maxCount != 0L) {
                child.queueStatus = QueueStatus.PARTICIPATES
                activeCountsByCategory[catId] = activeCount + 1
                statusChanged = true
            } else if (child.queueStatus == QueueStatus.PARTICIPATES && activeCount > maxCount && maxCount != 0L) {
                child.queueStatus = QueueStatus.IN_QUEUE
                activeCountsByCategory[catId] = activeCount - 1
                statusChanged = true
            }

            if (statusChanged) modifiedChildren.add(child)
        }

        if (modifiedChildren.isNotEmpty()) childToDirectionRepository.saveAll(modifiedChildren)
    }

    @Transactional
    override fun deleteById(id: Long): String {
        getById(id, message = "Компетенция с ID '${id}' не найдена.")!!.run {
            this.iconId?.run { documentsApiResolver.deleteDocument(this) }
            repository.delete(this)

            return "Компетенция удалена успешно."
        }
    }

    @Transactional
    override fun deleteAll(): String {
        getAll().forEach { direction ->
            direction.iconId?.run { documentsApiResolver.deleteDocument(this) }
            repository.delete(direction)
        }

        return "Все компетенции удалены успешно."
    }
}