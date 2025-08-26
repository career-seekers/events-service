package org.careerseekers.cseventsservice.services

import jakarta.transaction.Transactional
import org.careerseekers.cseventsservice.cache.UsersCacheClient
import org.careerseekers.cseventsservice.dto.platforms.ChangePlatformOwnerDto
import org.careerseekers.cseventsservice.dto.platforms.CreatePlatformDto
import org.careerseekers.cseventsservice.dto.platforms.UpdatePlatformDto
import org.careerseekers.cseventsservice.entities.Platforms
import org.careerseekers.cseventsservice.exceptions.NotFoundException
import org.careerseekers.cseventsservice.mappers.PlatformsMapper
import org.careerseekers.cseventsservice.repositories.PlatformsRepository
import org.careerseekers.cseventsservice.services.interfaces.CrudService
import org.springframework.stereotype.Service

@Service
class PlatformsService(
    override val repository: PlatformsRepository,
    private val usersCacheClient: UsersCacheClient,
    private val platformsMapper: PlatformsMapper,
) : CrudService<Platforms, Long, CreatePlatformDto, UpdatePlatformDto> {

    @Transactional
    override fun create(item: CreatePlatformDto): Platforms {
        return usersCacheClient.getItemFromCache(item.userId)?.let {
            // TODO("Made kafka notification about platform creation")
            repository.save(platformsMapper.platformFromDto(item))
        } ?: throw NotFoundException("User with id ${item.userId} not found.")
    }

    @Transactional
    override fun createAll(items: List<CreatePlatformDto>): String {
        for (dto in items) {
            create(dto)
        }

        return "Platforms created successfully."
    }

    @Transactional
    override fun update(item: UpdatePlatformDto): String {
        getById(item.id, message = "Platform with id ${item.id} not found")?.apply {
            item.fullName?.let { fullName = it }
            item.shortName?.let { shortName = it }
            item.address?.let { address = it }
        }

        return "Platform data updated successfully."
    }

    @Transactional
    fun updatePlatformOwner(item: ChangePlatformOwnerDto): String {
        getById(item.id, message = "Platform with id ${item.id} not found")?.apply {
            usersCacheClient.getItemFromCache(item.userId)?.let {
                userId = it.id
            } ?: throw NotFoundException("User with id ${item.userId} not found.")
        }

        return "Platform owner updated successfully."
    }

    @Transactional
    fun updatePlatformVerification(platformId: Long): String {
        getById(platformId, message = "Platform with id $platformId not found")?.apply {
            verified = !verified
        }

        return "Platform verification updated successfully."
    }

    @Transactional
    override fun deleteById(id: Long): String {
        getById(id, message = "Platform with id $id not found")?.let {
            repository.delete(it)
        }

        return "Platform deleted successfully."
    }

    @Transactional
    override fun deleteAll(): String {
        repository.deleteAll()

        return "All platforms deleted successfully."
    }
}