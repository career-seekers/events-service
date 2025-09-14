package org.careerseekers.cseventsservice.mocks.generators

import org.careerseekers.cseventsservice.dto.platforms.CreatePlatformDto
import org.careerseekers.cseventsservice.entities.Platforms
import org.careerseekers.cseventsservice.mocks.generators.MocksGenerator.randomString
import kotlin.random.Random.Default.nextLong

object PlatformGenerator {
    fun createPlatform() = Platforms(
        id = nextLong(1, 100000),
        fullName = randomString(12),
        shortName = randomString(12),
        address = randomString(12),
        verified = false,
        userId = nextLong(1, 100000),
        email = randomString(12),
        website = randomString(12),
    )

    fun createPlatformDto(item: Platforms) = CreatePlatformDto(
        fullName = item.fullName,
        shortName = item.shortName,
        address = item.address,
        verified = item.verified,
        userId = item.userId,
        email = randomString(12),
        website = randomString(12),
    )
}