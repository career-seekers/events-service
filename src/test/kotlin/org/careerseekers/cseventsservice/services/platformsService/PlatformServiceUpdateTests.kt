package org.careerseekers.cseventsservice.services.platformsService

import io.mockk.every
import io.mockk.verify
import org.assertj.core.api.Assertions.assertThat
import org.careerseekers.cseventsservice.dto.platforms.ChangePlatformOwnerDto
import org.careerseekers.cseventsservice.dto.platforms.UpdatePlatformDto
import org.careerseekers.cseventsservice.exceptions.NotFoundException
import org.careerseekers.cseventsservice.mocks.PlatformServiceMocks
import org.careerseekers.cseventsservice.mocks.generators.CachedUsersGenerator.createUser
import org.careerseekers.cseventsservice.mocks.generators.PlatformGenerator.createPlatform
import org.junit.jupiter.api.Nested
import kotlin.test.Test
import kotlin.test.assertFailsWith

class PlatformServiceUpdateTests : PlatformServiceMocks() {

    @Nested
    inner class UpdateTests {

        @Test
        fun `Should update a platform and return String`() {
            val platform = createPlatform().copy(shortName = "ShortName")
            val updateDto = UpdatePlatformDto(
                id = platform.id,
                fullName = "New full name",
                shortName = null,
                address = "New address",
            )

            every { serviceUnderTest.getById(any(), any(), any()) } returns platform
            every { repository.save(any()) } returns platform

            val result = serviceUnderTest.update(updateDto)

            assertThat(result).isEqualTo("Platform data updated successfully.")

            assertThat(platform.fullName).isEqualTo("New full name")
            assertThat(platform.shortName).isEqualTo("ShortName")
            assertThat(platform.address).isEqualTo("New address")

            verify { serviceUnderTest.getById(any(), any(), any()) }
            verify { repository.save(any()) }
        }

        @Test
        fun `Should return NotFoundException when platform is null`() {
            val platform = createPlatform().copy(fullName = "Full name", shortName = "ShortName", address = "Address")
            val updateDto = UpdatePlatformDto(
                id = platform.id,
                fullName = null,
                shortName = null,
                address = null
            )

            every {
                serviceUnderTest.getById(
                    any(),
                    any(),
                    any()
                )
            } throws NotFoundException("Platform with id ${platform.id} not found")

            val exception = assertFailsWith<NotFoundException> {
                serviceUnderTest.update(updateDto)
            }

            assertThat(exception.message).isEqualTo("Platform with id ${platform.id} not found")

            assertThat(platform.fullName).isEqualTo("Full name")
            assertThat(platform.shortName).isEqualTo("ShortName")
            assertThat(platform.address).isEqualTo("Address")

            verify { serviceUnderTest.getById(any(), any(), any()) }
            verify(exactly = 0) { repository.save(any()) }
        }
    }

    @Nested
    inner class UpdatePlatformOwnerTests {

        private val user = createUser()
        private val platform = createPlatform().copy(userId = 1L)
        private val dto = ChangePlatformOwnerDto(
            id = platform.id,
            userId = user.id,
        )

        @Test
        fun `Should update platform owner and return String`() {
            every { serviceUnderTest.getById(any(), any(), any()) } returns platform
            every { usersCacheClient.getItemFromCache(any()) } returns user
            every { repository.save(any()) } returns platform

            val result = serviceUnderTest.updatePlatformOwner(dto)

            assertThat(result).isNotNull.isEqualTo("Platform owner updated successfully.")

            verify { serviceUnderTest.getById(any(), any(), any()) }
            verify { usersCacheClient.getItemFromCache(any()) }
            verify { repository.save(any()) }
        }

        @Test
        fun `Should return NotFoundException when platform is null`() {
            every {
                serviceUnderTest.getById(
                    any(),
                    any(),
                    any()
                )
            } throws NotFoundException("Platform with id ${platform.id} not found")

            val exception = assertFailsWith<NotFoundException> {
                serviceUnderTest.updatePlatformOwner(dto)
            }

            assertThat(exception.message).isEqualTo("Platform with id ${platform.id} not found")

            verify { serviceUnderTest.getById(any(), any(), any()) }
            verify(exactly = 0) { usersCacheClient.getItemFromCache(any()) }
            verify(exactly = 0) { repository.save(any()) }
        }

        @Test
        fun `Should throw NotFoundException when user not found in database`() {
            every { serviceUnderTest.getById(any(), any(), any()) } returns platform
            every { usersCacheClient.getItemFromCache(any()) } throws NotFoundException("User with id ${user.id} not found.")

            val exception = assertFailsWith<NotFoundException> {
                serviceUnderTest.updatePlatformOwner(dto)
            }

            assertThat(exception.message).isEqualTo("User with id ${user.id} not found.")

            verify { serviceUnderTest.getById(any(), any(), any()) }
            verify { usersCacheClient.getItemFromCache(any()) }
            verify(exactly = 0) { repository.save(any()) }
        }
    }
}