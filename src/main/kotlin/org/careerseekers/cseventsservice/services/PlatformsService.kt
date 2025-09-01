package org.careerseekers.cseventsservice.services

import org.careerseekers.cseventsservice.cache.UsersCacheClient
import org.careerseekers.cseventsservice.dto.PlatformCreation
import org.careerseekers.cseventsservice.dto.platforms.ChangePlatformOwnerDto
import org.careerseekers.cseventsservice.dto.platforms.CreatePlatformDto
import org.careerseekers.cseventsservice.dto.platforms.UpdatePlatformDto
import org.careerseekers.cseventsservice.entities.Platforms
import org.careerseekers.cseventsservice.exceptions.NotFoundException
import org.careerseekers.cseventsservice.io.converters.extensions.toKafkaPlatformDto
import org.careerseekers.cseventsservice.mappers.PlatformsMapper
import org.careerseekers.cseventsservice.repositories.PlatformsRepository
import org.careerseekers.cseventsservice.services.interfaces.CrudService
import org.careerseekers.cseventsservice.services.kafka.producers.PlatformCreationKafkaProducer
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class PlatformsService(
    override val repository: PlatformsRepository,
    private val usersCacheClient: UsersCacheClient,
    private val platformsMapper: PlatformsMapper,
    private val platformCreationKafkaProducer: PlatformCreationKafkaProducer
) : CrudService<Platforms, Long, CreatePlatformDto, UpdatePlatformDto> {

    @Transactional
    override fun create(item: CreatePlatformDto): Platforms {
        return usersCacheClient.getItemFromCache(item.userId)?.let {
            val platform = repository.save(platformsMapper.platformFromDto(item))

            platformCreationKafkaProducer.sendMessage(PlatformCreation(platform.toKafkaPlatformDto()))

            platform
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

            repository.save(this)
        }

        return "Platform data updated successfully."
    }

    @Transactional
    fun updatePlatformOwner(item: ChangePlatformOwnerDto): String {
        getById(item.id, message = "Platform with id ${item.id} not found")?.apply {
            usersCacheClient.getItemFromCache(item.userId)?.let {
                userId = it.id
                repository.save(this)
            } ?: throw NotFoundException("User with id ${item.userId} not found.")
        }

        return "Platform owner updated successfully."
    }

    @Transactional
    fun updatePlatformVerification(platformId: Long): String {
        getById(platformId, message = "Platform with id $platformId not found")?.apply {
            verified = !verified
            repository.save(this)
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