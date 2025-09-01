package org.careerseekers.cseventsservice.mocks

import io.mockk.mockk
import io.mockk.spyk
import org.careerseekers.cseventsservice.cache.UsersCacheClient
import org.careerseekers.cseventsservice.mappers.PlatformsMapper
import org.careerseekers.cseventsservice.repositories.PlatformsRepository
import org.careerseekers.cseventsservice.services.PlatformsService
import org.careerseekers.cseventsservice.services.kafka.producers.PlatformCreationKafkaProducer

open class PlatformServiceMocks {
    protected val repository = mockk<PlatformsRepository>()
    protected val usersCacheClient = mockk<UsersCacheClient>()
    protected val platformsMapper = mockk<PlatformsMapper>()
    protected val platformCreationKafkaProducer = mockk<PlatformCreationKafkaProducer>()

    protected val serviceUnderTest = spyk(
        PlatformsService(
            repository = repository,
            usersCacheClient = usersCacheClient,
            platformsMapper = platformsMapper,
            platformCreationKafkaProducer = platformCreationKafkaProducer
        )
    )
}