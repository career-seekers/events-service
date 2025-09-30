package org.careerseekers.cseventsservice.services

import org.careerseekers.cseventsservice.dto.directions.categories.CreateAgeCategory
import org.careerseekers.cseventsservice.dto.directions.categories.UpdateAgeCategoryDto
import org.careerseekers.cseventsservice.entities.DirectionAgeCategories
import org.careerseekers.cseventsservice.mappers.DirectionAgeCategoriesMapper
import org.careerseekers.cseventsservice.repositories.DirectionAgeCategoriesRepository
import org.careerseekers.cseventsservice.repositories.DirectionsRepository
import org.careerseekers.cseventsservice.services.interfaces.crud.ICreateService
import org.careerseekers.cseventsservice.services.interfaces.crud.IDeleteService
import org.careerseekers.cseventsservice.services.interfaces.crud.IReadService
import org.careerseekers.cseventsservice.services.interfaces.crud.IUpdateService
import org.springframework.context.annotation.Lazy
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class DirectionAgeCategoriesService(
    override val repository: DirectionAgeCategoriesRepository,
    private val mapper: DirectionAgeCategoriesMapper,
    @param:Lazy private val directionsService: DirectionsService,
    private val directionsRepository: DirectionsRepository
) : IReadService<DirectionAgeCategories, Long>,
    ICreateService<DirectionAgeCategories, Long, CreateAgeCategory>,
    IUpdateService<DirectionAgeCategories, Long, UpdateAgeCategoryDto>,
    IDeleteService<DirectionAgeCategories, Long> {

    @Transactional
    override fun create(item: CreateAgeCategory) = repository.save(mapper.ageCategoryFromDto(item))

    @Transactional
    override fun update(item: UpdateAgeCategoryDto): String {
        getById(item.id, message = "Возрастная категория с ID ${item.id} не найдена.")!!.apply {
            item.maxParticipantsCount?.let { maxParticipantsCount = it }
            item.currentParticipantsCount?.let {
                val oldCount = this.currentParticipantsCount
                val diff = it - oldCount

                this.direction.participantsCount += diff
                this.currentParticipantsCount = it

                directionsRepository.save(this.direction)
                repository.save(this)
            }
        }.also { directionsService.updateDirectionParticipants(it.direction) }

        return "Данные по возрастной линейке обновлены успешно."
    }

    @Transactional
    override fun deleteById(id: Long) {
        getById(id, message = "Direction age category with id $id not found")?.let { category ->
            category.direction.ageCategories
                ?.removeAll { it == category }
                .also { directionsRepository.save(category.direction) }

            repository.delete(category)
        }
    }

    override fun deleteAll(): String {
        directionsService.getAll().forEach { direction ->
            direction.ageCategories?.clear()
        }
        repository.deleteAll()
        return "All direction age categories deleted"
    }
}