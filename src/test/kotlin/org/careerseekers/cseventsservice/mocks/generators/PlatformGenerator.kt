package org.careerseekers.cseventsservice.mocks.generators

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
    )
}