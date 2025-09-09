package org.careerseekers.cseventsservice.services

import org.careerseekers.cseventsservice.cache.UsersCacheClient
import org.careerseekers.cseventsservice.dto.directions.CreateDirectionDto
import org.careerseekers.cseventsservice.dto.directions.UpdateDirectionDto
import org.careerseekers.cseventsservice.entities.Directions
import org.careerseekers.cseventsservice.enums.DirectionAgeCategory
import org.careerseekers.cseventsservice.exceptions.NotFoundException
import org.careerseekers.cseventsservice.mappers.DirectionsMapper
import org.careerseekers.cseventsservice.repositories.DirectionsRepository
import org.careerseekers.cseventsservice.services.interfaces.CrudService
import org.careerseekers.cseventsservice.utils.DocumentsApiResolver
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import kotlin.also

@Service
class DirectionsService(
    override val repository: DirectionsRepository,
    private val directionsMapper: DirectionsMapper,
    private val documentsApiResolver: DocumentsApiResolver,
    private val usersCacheClient: UsersCacheClient,
) : CrudService<Directions, Long, CreateDirectionDto, UpdateDirectionDto> {

    fun getByUserId(userId: Long): List<Directions> = repository.findByUserId(userId)

    fun getByExpertId(expertId: Long): List<Directions> = repository.findByExpertId(expertId)

    fun getByAgeCategory(ageCategory: DirectionAgeCategory): List<Directions> = repository.findByAgeCategory(ageCategory)

    @Transactional
    override fun create(item: CreateDirectionDto): Directions {
        item.userId?.let { usersCacheClient.getItemFromCache(it) ?: throw NotFoundException("User with id $it not found.") }
        item.expertId?.let { usersCacheClient.getItemFromCache(it) ?: throw NotFoundException("User with id $it not found.") }

        return repository.save(
            directionsMapper.directionFromDto(item.copy(
                iconId = item.icon?.let { documentsApiResolver.loadDocId("uploadDirectionIcon", it) }
            ))
        )
    }

    @Transactional
    override fun createAll(items: List<CreateDirectionDto>): String {
        for (item in items) {
            create(item)
        }

        return "All directions have been created."
    }

    @Transactional
    override fun update(item: UpdateDirectionDto): String {
        getById(item.id, message = "Direction with id '${item.id}' not found.")!!.apply {
            item.name?.let { name = it }
            item.description?.let { description = it }
            item.icon?.let {
                val oldIconId = this.iconId

                iconId = documentsApiResolver.loadDocId("uploadDirectionIcon", it)
                oldIconId?.let { iconId -> documentsApiResolver.deleteDocument(iconId) }
            }
            item.expertId?.let {
                usersCacheClient.getItemFromCache(it) ?: throw NotFoundException("User with id $it not found.")
                expertId = it
            }
        }.also(repository::save)

        return "Direction updated successfully."
    }

    @Transactional
    override fun deleteById(id: Long): String {
        getById(id, message = "Direction with id '${id}' not found.")!!.run {
            this.iconId?.run { documentsApiResolver.deleteDocument(this) }
            repository.delete(this)

            return "Direction deleted successfully."
        }
    }

    @Transactional
    override fun deleteAll(): String {
        getAll().forEach { direction ->
            direction.iconId?.run { documentsApiResolver.deleteDocument(this) }
            repository.delete(direction)
        }

        return "All directions have been deleted."
    }
}