package org.careerseekers.cseventsservice.services.platformsService

import io.mockk.Runs
import io.mockk.every
import io.mockk.just
import io.mockk.verify
import org.assertj.core.api.Assertions.assertThat
import org.careerseekers.cseventsservice.exceptions.NotFoundException
import org.careerseekers.cseventsservice.mocks.PlatformServiceMocks
import org.careerseekers.cseventsservice.mocks.generators.PlatformGenerator.createPlatform
import org.junit.jupiter.api.Nested
import kotlin.test.Test
import kotlin.test.assertFailsWith

class PlatformServiceDeleteTests : PlatformServiceMocks() {

    @Nested
    inner class DeleteByIdTests {

        @Test
        fun `Should delete platform and return String`() {
            val platform = createPlatform()

            every { serviceUnderTest.getById(any(), any(), any()) } returns platform
            every { repository.delete(any()) } just Runs

            val result = serviceUnderTest.deleteById(platform.id)

            assertThat(result).isNotNull.isEqualTo("Platform deleted successfully.")

            verify { serviceUnderTest.getById(any(), any(), any()) }
            verify { repository.delete(any()) }
        }

        @Test
        fun `Should return NotFoundException when platform is null`() {
            val platform = createPlatform()

            every { serviceUnderTest.getById(any(), any(), any()) } throws NotFoundException("Platform with id ${platform.id} not found")

            val exception = assertFailsWith<NotFoundException> {
                serviceUnderTest.deleteById(platform.id)
            }

            assertThat(exception.message).isEqualTo("Platform with id ${platform.id} not found")

            verify { serviceUnderTest.getById(any(), any(), any()) }
            verify(exactly = 0) { repository.delete(any()) }
        }
    }
}