package org.careerseekers.cseventsservice.services

import org.careerseekers.cseventsservice.dto.directions.childToDirection.CreateChildWithDirectionDto
import org.careerseekers.cseventsservice.dto.directions.childToDirection.UpdateChildToDirectionsDto
import org.careerseekers.cseventsservice.entities.ChildToDirection
import org.careerseekers.cseventsservice.enums.QueueStatus
import org.careerseekers.cseventsservice.exceptions.BadRequestException
import org.careerseekers.cseventsservice.mappers.ChildToDirectionMapper
import org.careerseekers.cseventsservice.repositories.ChildToDirectionRepository
import org.careerseekers.cseventsservice.services.interfaces.crud.ICreateService
import org.careerseekers.cseventsservice.services.interfaces.crud.IDeleteService
import org.careerseekers.cseventsservice.services.interfaces.crud.IReadService
import org.careerseekers.cseventsservice.services.interfaces.crud.IUpdateService
import org.springframework.dao.OptimisticLockingFailureException
import org.springframework.retry.annotation.Backoff
import org.springframework.retry.annotation.Retryable
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class ChildToDirectionService(
    override val repository: ChildToDirectionRepository,
    private val directionService: DirectionsService,
    private val directionAgeCategoriesService: DirectionAgeCategoriesService,
    private val childToDirectionMapper: ChildToDirectionMapper
) : IReadService<ChildToDirection, Long>,
    ICreateService<ChildToDirection, Long, CreateChildWithDirectionDto>,
    IUpdateService<ChildToDirection, Long, UpdateChildToDirectionsDto>,
    IDeleteService<ChildToDirection, Long> {

    fun getByChildId(childId: Long): List<ChildToDirection> = repository.findByChildId(childId)

    fun getByDirectionId(directionId: Long): List<ChildToDirection> = repository.findByDirectionId(directionId)

    @Retryable(value = [OptimisticLockingFailureException::class], maxAttempts = 5, backoff = Backoff(delay = 500))
    @Transactional
    override fun create(item: CreateChildWithDirectionDto): ChildToDirection {
        val direction =
            directionService.getById(item.directionId, message = "Компетенция с ID ${item.directionId} не найдена.")!!

        val ageCategory = directionAgeCategoriesService.getById(
            item.directionAgeCategoryId,
            message = "Возрастная категория с ID ${item.directionAgeCategoryId} не найдена."
        )!!

        if (ageCategory.direction != direction) throw BadRequestException("Возрастная категория должна принадлежать компетенции.")

        item.apply {
            this.queueStatus = QueueStatus.IN_QUEUE
            this.direction = direction
            this.directionAgeCategory = ageCategory
        }

        return repository.save(childToDirectionMapper.objectFromDto(item))
            .also { directionService.updateDirectionParticipants(direction) }
    }

    @Transactional
    override fun update(item: UpdateChildToDirectionsDto): String {
        getById(item.id, message = "Запись ребенка на компетенцию с ID ${item.id} не найдена.")!!.apply {
            item.status?.let { status = it }
            item.queueStatus?.let { queueStatus = it }
        }.also(repository::save)

        return "Запись об участии в компетенции обновлена успешно."
    }

    @Transactional
    override fun deleteById(id: Long): String {
        getById(id, message = "Запись ребенка на компетенцию с ID $id не найдена.")!!.apply {
            repository.delete(this)
        }.also {
            it.direction.let { direction -> directionService.updateDirectionParticipants(direction) }
        }

        return "Запись на компетенцию отменена."
    }

    @Transactional
    fun deleteByChildId(childId: Long): String {
        getByChildId(childId).forEach { item ->
            item.apply {
                repository.delete(this)
            }.also {
                it.direction.let { direction -> directionService.updateDirectionParticipants(direction) }
            }
        }

        return "Все записи ребёнка с ID $childId на компетенцию отменены."
    }

    override fun deleteAll(): String {
        repository.deleteAll()

        return "Все записи на компетенции удалены."
    }
}