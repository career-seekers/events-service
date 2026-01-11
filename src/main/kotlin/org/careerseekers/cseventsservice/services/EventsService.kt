package org.careerseekers.cseventsservice.services

import org.careerseekers.cseventsservice.annotations.EventVerification
import org.careerseekers.cseventsservice.dto.events.CreateEventDto
import org.careerseekers.cseventsservice.dto.events.EventsFilterDto
import org.careerseekers.cseventsservice.dto.events.UpdateEventDto
import org.careerseekers.cseventsservice.dto.events.UpdateEventVerificationDto
import org.careerseekers.cseventsservice.entities.Events
import org.careerseekers.cseventsservice.enums.VerificationStatus
import org.careerseekers.cseventsservice.exceptions.NotFoundException
import org.careerseekers.cseventsservice.mappers.EventsMapper
import org.careerseekers.cseventsservice.repositories.EventsRepository
import org.careerseekers.cseventsservice.repositories.spec.EventsSpecifications.hasAgeCategoryName
import org.careerseekers.cseventsservice.repositories.spec.EventsSpecifications.hasDirectionName
import org.careerseekers.cseventsservice.repositories.spec.EventsSpecifications.hasEndDateTimeBefore
import org.careerseekers.cseventsservice.repositories.spec.EventsSpecifications.hasEventFormat
import org.careerseekers.cseventsservice.repositories.spec.EventsSpecifications.hasEventType
import org.careerseekers.cseventsservice.repositories.spec.EventsSpecifications.hasName
import org.careerseekers.cseventsservice.repositories.spec.EventsSpecifications.hasStartDateTimeAfter
import org.careerseekers.cseventsservice.repositories.spec.EventsSpecifications.hasVerified
import org.careerseekers.cseventsservice.services.interfaces.PagedCrudService
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.jpa.domain.Specification
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.time.ZonedDateTime

@Service
class EventsService(
    override val repository: EventsRepository,
    private val eventsMapper: EventsMapper,
    private val directionsService: DirectionsService,
    private val directionAgeCategoriesService: DirectionAgeCategoriesService,
) : PagedCrudService<Events, Long, CreateEventDto, UpdateEventDto, EventsFilterDto> {
    private fun basicNotFoundMessage(id: Long): String = "Событие с ID $id не найдено."

    override fun getAll(filters: EventsFilterDto, pageable: Pageable): Page<Events> {
        val specs = listOfNotNull(
            hasName(filters.name),
            hasEventType(filters.eventType),
            hasEventFormat(filters.eventFormat),
            hasVerified(filters.verified),
            hasStartDateTimeAfter(filters.startDateTime),
            hasEndDateTimeBefore(filters.endDateTime),
            hasDirectionName(filters.directionName),
            hasAgeCategoryName(filters.ageCategory)
        )

        return repository.findAll(Specification.allOf(specs), pageable)
    }

    fun getByDirectionId(id: Long): List<Events> = repository.findByDirectionId(id)

    fun getByAgeCategoryId(id: Long): List<Events> = repository.findByDirectionAgeCategoryId(id)

    @Transactional
    override fun create(item: CreateEventDto): Events {
        val direction =
            directionsService.getById(item.directionId, message = "Компетенция с ID ${item.directionId} не найдена.")!!
        val ageCategory = directionAgeCategoriesService.getById(
            item.directionAgeCategoryId,
            message = "Возрастная категория с ID ${item.directionAgeCategoryId} не найдена."
        )!!

        return repository.save<Events>(eventsMapper.eventFromDto(item, direction, ageCategory, VerificationStatus.UNCHECKED))
    }

    @Transactional
    override fun createAll(items: List<CreateEventDto>): String {
        val batchSize = 50
        val savedEntities = mutableListOf<Events>()

        val directions = directionsService.getAll().associateBy { it.id }
        val ageCategories = directionAgeCategoriesService.getAll().associateBy { it.id }

        for (batchStart in items.indices step batchSize) {
            val batch = items.subList(batchStart, minOf(batchStart + batchSize, items.size))

            val entities = batch.map { item ->
                val direction = directions[item.directionId]
                    ?: throw NotFoundException("Компетенция с ID ${item.directionId} не найдена.")
                val ageCategory = ageCategories[item.directionAgeCategoryId]
                    ?: throw NotFoundException("Возрастная категория с ID ${item.directionAgeCategoryId} не найдена.")
                eventsMapper.eventFromDto(item, direction, ageCategory, VerificationStatus.UNCHECKED)
            }

            savedEntities += repository.saveAll(entities)
            repository.flush()
        }

        return "Создано событий: ${savedEntities.size}"
    }

    @Transactional
    override fun update(item: UpdateEventDto): String {
        getById(item.id, message = basicNotFoundMessage(item.id))!!.apply {
            item.name?.let { name = it }
            item.shortDescription?.let { shortDescription = it }
            item.eventType?.let { eventType = it }
            item.eventFormats?.let { eventFormat = it }
            item.eventVenue?.let { eventVenue = it }
            item.description?.let { description = it }
            item.directionAgeCategoryId?.let {
                directionAgeCategory = directionAgeCategoriesService.getById(
                    item.directionAgeCategoryId,
                    message = "Возрастная категория с ID ${item.directionAgeCategoryId} не найдена."
                )!!
            }
            startDateTime = item.startDateTime ?: this.startDateTime
            updatedAt = ZonedDateTime.now()
            repository.save(this)
            return "Информация о событии обновлена."
        }
    }

    @Transactional
    @EventVerification
    fun verifyEvent(item: UpdateEventVerificationDto): String {
        getById(item.id, message = basicNotFoundMessage(item.id))!!.apply {
            verified = item.verified

            updatedAt = ZonedDateTime.now()
            repository.save(this)
            return "Информация о событии обновлена."
        }
    }

    @Transactional
    override fun deleteById(id: Long): String {
        getById(id, message = basicNotFoundMessage(id))!!.let {
            repository.delete(it)
        }
        return "Событие удалено успешно."
    }

    @Transactional
    override fun deleteAll(): String {
        super.deleteAll()
        return "Все события удалены успешно."
    }
}