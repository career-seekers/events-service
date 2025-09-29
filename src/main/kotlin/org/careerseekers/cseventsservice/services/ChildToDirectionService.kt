package org.careerseekers.cseventsservice.services

import org.careerseekers.cseventsservice.dto.directions.childToDirection.LinkChildWithDirectionDto
import org.careerseekers.cseventsservice.entities.ChildToDirection
import org.careerseekers.cseventsservice.enums.QueueStatus
import org.careerseekers.cseventsservice.exceptions.BadRequestException
import org.careerseekers.cseventsservice.mappers.ChildToDirectionMapper
import org.careerseekers.cseventsservice.repositories.ChildToDirectionRepository
import org.careerseekers.cseventsservice.services.interfaces.crud.ICreateService
import org.careerseekers.cseventsservice.services.interfaces.crud.IReadService
import org.springframework.stereotype.Service

@Service
class ChildToDirectionService(
    override val repository: ChildToDirectionRepository,
    private val directionService: DirectionsService,
    private val directionAgeCategoriesService: DirectionAgeCategoriesService,
    private val childToDirectionMapper: ChildToDirectionMapper
) : IReadService<ChildToDirection, Long>,
    ICreateService<ChildToDirection, Long, LinkChildWithDirectionDto> {

    fun getByChildId(childId: Long): List<ChildToDirection> = repository.findByChildId(childId)

    fun getByDirectionId(directionId: Long): List<ChildToDirection> = repository.findByDirectionId(directionId)

    override fun create(item: LinkChildWithDirectionDto): ChildToDirection {
        val direction =
            directionService.getById(item.directionId, message = "Компетенция с ID ${item.directionId} не найдена.")!!

        val ageCategory = directionAgeCategoriesService.getById(
            item.directionAgeCategoryId,
            message = "Возрастная категория с ID ${item.directionAgeCategoryId} не найдена."
        )!!

        if (ageCategory.direction != direction) throw BadRequestException("Возрастная категория должна принадлежать компетенции.")

        item.apply {
            queueStatus =
                if (ageCategory.currentParticipantsCount >= ageCategory.maxParticipantsCount) QueueStatus.IN_QUEUE else QueueStatus.PARTICIPATES

            item.direction = direction
            item.directionAgeCategory = ageCategory
        }

        return repository.save(childToDirectionMapper.objectFromDto(item)).also {
            ageCategory.increaseCurrentParticipantsCount()
        }
    }
}