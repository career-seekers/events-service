package org.careerseekers.cseventsservice.services.platformsService

import io.mockk.every
import io.mockk.verify
import org.assertj.core.api.Assertions.assertThat
import org.careerseekers.cseventsservice.exceptions.NotFoundException
import org.careerseekers.cseventsservice.mocks.PlatformServiceMocks
import org.careerseekers.cseventsservice.mocks.generators.CachedUsersGenerator.createUser
import org.careerseekers.cseventsservice.mocks.generators.PlatformGenerator.createPlatform
import org.careerseekers.cseventsservice.mocks.generators.PlatformGenerator.createPlatformDto
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import kotlin.test.assertFailsWith

class PlatformServiceCreateTests : PlatformServiceMocks() {

    @Nested
    inner class CreateTests {

        @Test
        fun `Should create and return platform`() {
            val user = createUser()
            val platform = createPlatform().copy(userId = user.id)
            val platformDto = createPlatformDto(platform)

            every { usersCacheClient.getItemFromCache(any()) } returns user
            every { platformsMapper.platformFromDto(any()) } returns platform
            every { repository.findByEmail(any()) } returns null
            every { repository.save(any()) } returns platform
            every { platformCreationKafkaProducer.sendMessage(any()) } returns Unit

            val result = serviceUnderTest.create(platformDto)

            assertThat(result).isNotNull.isEqualTo(platform)

            verify { usersCacheClient.getItemFromCache(platformDto.userId) }
            verify { platformsMapper.platformFromDto(any()) }
            verify { repository.save(platform) }
        }

        @Test
        fun `Should throw NotFoundException when user not found`() {
            val userId = 1L
            val platform = createPlatform().copy(userId = userId)
            val platformDto = createPlatformDto(platform)

            every { usersCacheClient.getItemFromCache(any()) } returns null

            val exception = assertFailsWith<NotFoundException> {
                serviceUnderTest.create(platformDto)
            }

            assertThat(exception.message).isEqualTo("Пользователь с ID $userId не найден.")

            verify { usersCacheClient.getItemFromCache(platformDto.userId) }

            verify(exactly = 0) { platformsMapper.platformFromDto(any()) }
            verify(exactly = 0) { repository.save(platform) }
        }
    }

    @Nested
    inner class CreateAllTests {

        @Test
        fun `Should create all platforms`() {
            val dummyPlatform = createPlatform()
            val platforms = List(5) { createPlatformDto(dummyPlatform) }

            every { serviceUnderTest.create(any()) } returns dummyPlatform

            val result = serviceUnderTest.createAll(platforms)

            assertThat(result).isNotNull.isEqualTo("Все площадки успешно созданы.")

            verify(exactly = 5) { serviceUnderTest.create(any())  }
        }
    }
}