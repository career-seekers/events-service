package org.careerseekers.cseventsservice.services.platformsService

import io.mockk.every
import io.mockk.verify
import org.assertj.core.api.Assertions.assertThat
import org.careerseekers.cseventsservice.entities.Platforms
import org.careerseekers.cseventsservice.mocks.PlatformServiceMocks
import org.careerseekers.cseventsservice.mocks.generators.PlatformGenerator.createPlatform
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test

class PlatformServiceGetTests : PlatformServiceMocks() {

    @Nested
    inner class GetAllTests {

        @Test
        fun `Should return all platforms`() {
            val platform = List<Platforms>(5) { createPlatform() }

            every { repository.findAll() } returns platform

            val result = serviceUnderTest.getAll()

            assertThat(result).isNotNull.isEqualTo(platform)

            verify { repository.findAll() }
        }

        @Test
        fun `Should return empty list`() {
            val platform = emptyList<Platforms>()

            every { repository.findAll() } returns platform

            val result = serviceUnderTest.getAll()

            assertThat(result).isNotNull.isEmpty()

            verify { repository.findAll() }
        }
    }
}