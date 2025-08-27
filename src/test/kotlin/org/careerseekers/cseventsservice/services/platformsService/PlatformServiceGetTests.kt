package org.careerseekers.cseventsservice.services.platformsService

import io.mockk.every
import io.mockk.verify
import org.assertj.core.api.Assertions.assertThat
import org.careerseekers.cseventsservice.entities.Platforms
import org.careerseekers.cseventsservice.exceptions.NotFoundException
import org.careerseekers.cseventsservice.mocks.PlatformServiceMocks
import org.careerseekers.cseventsservice.mocks.generators.PlatformGenerator.createPlatform
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import java.util.Optional
import kotlin.test.assertFailsWith

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

    @Nested
    inner class GetByIdTests {

        @Test
        fun `Should return platform`() {
            val platform = createPlatform().copy(id = 1L)

            every { repository.findById(platform.id) } returns Optional.of(platform)

            val result = serviceUnderTest.getById(platform.id, throwable = true)

            assertThat(result).isNotNull.isEqualTo(platform)

            verify { repository.findById(platform.id) }
        }

        @Test
        fun `Should throw NotFoundException when platform is null and throwable is true`() {
            val dummyId = 1L
            every { repository.findById(any()) } returns Optional.empty()

            val exception = assertFailsWith<NotFoundException> {
                serviceUnderTest.getById(
                    dummyId,
                    throwable = true,
                    message = "Platform with id $dummyId not found."
                )
            }

            assertThat(exception.message).isEqualTo("Platform with id $dummyId not found.")

            verify { repository.findById(dummyId) }
        }

        @Test
        fun `Should return null when platform is null and throwable is false`() {
            val dummyId = 1L
            every { repository.findById(any<Long>()) } returns Optional.empty()

            val result = serviceUnderTest.getById(dummyId, throwable = false)

            assertThat(result).isNull()

            verify { repository.findById(dummyId) }
        }
    }
}