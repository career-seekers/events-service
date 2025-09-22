package org.careerseekers.cseventsservice.services

import org.careerseekers.cseventsservice.cache.UsersCacheClient
import org.careerseekers.cseventsservice.dto.PlatformCreation
import org.careerseekers.cseventsservice.dto.platforms.ChangePlatformOwnerDto
import org.careerseekers.cseventsservice.dto.platforms.CreatePlatformDto
import org.careerseekers.cseventsservice.dto.platforms.UpdatePlatformDto
import org.careerseekers.cseventsservice.entities.Platforms
import org.careerseekers.cseventsservice.exceptions.DoubleRecordException
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

    fun getByUserId(userId: Long): Platforms? {
        usersCacheClient.getItemFromCache(userId) ?: throw NotFoundException("Пользователь с ID $userId не найден.")

        return repository.findByUserId(userId) ?: throw NotFoundException("Площадка с userID $userId не найдена.")
    }

    @Transactional
    override fun create(item: CreatePlatformDto): Platforms {
        return usersCacheClient.getItemFromCache(item.userId)?.let {
            repository.findByEmail(item.email)
                ?.let { throw DoubleRecordException("Площадка с электронной почтой ${item.email} уже существует.") }

            val platform = repository.save(platformsMapper.platformFromDto(item))

            platformCreationKafkaProducer.sendMessage(PlatformCreation(platform.toKafkaPlatformDto()))

            platform
        } ?: throw NotFoundException("Пользователь с ID ${item.userId} не найден.")
    }

    @Transactional
    override fun createAll(items: List<CreatePlatformDto>): String {
        for (dto in items) {
            create(dto)
        }

        return "Все площадки успешно созданы."
    }

    @Transactional
    override fun update(item: UpdatePlatformDto): String {
        getById(item.id, message = "Площадка с ID ${item.id} не найдена.")?.apply {
            item.fullName?.let { fullName = it }
            item.shortName?.let { shortName = it }
            item.address?.let { address = it }
            item.email?.let {
                if (it != this.email) {
                    repository.findByEmail(item.email)
                        ?.let { throw DoubleRecordException("Площадка с электронной почтой ${item.email} уже существует.") }
                    email = it
                }
            }
            item.website?.let { website = it }

            repository.save(this)
        }

        return "Данные площадки успешно обновлены."
    }

    @Transactional
    fun updatePlatformOwner(item: ChangePlatformOwnerDto): String {
        getById(item.id, message = "Площадка с ID ${item.id} не найдена.")?.apply {
            usersCacheClient.getItemFromCache(item.userId)?.let {
                userId = it.id
                repository.save(this)
            } ?: throw NotFoundException("Пользователь с ID ${item.userId} не найден.")
        }

        return "Владелец площадки изменен успешно."
    }

    @Transactional
    fun updatePlatformVerification(platformId: Long): String {
        getById(platformId, message = "Площадка с ID $platformId не найдена.")?.apply {
            verified = !verified
            repository.save(this)
        }

        return "Платформа верифицирована успешно."
    }

    @Transactional
    override fun deleteById(id: Long): String {
        getById(id, message = "Платформа с ID $id не найдена.")?.let {
            repository.delete(it)
        }

        return "Платформа удалена успешно."
    }

    @Transactional
    override fun deleteAll(): String {
        repository.deleteAll()

        return "Все платформы удалены успешно."
    }
}