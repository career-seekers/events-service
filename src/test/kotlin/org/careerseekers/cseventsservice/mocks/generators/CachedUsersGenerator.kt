package org.careerseekers.cseventsservice.mocks.generators

import org.careerseekers.cseventsservice.dto.UsersCacheDto
import org.careerseekers.cseventsservice.enums.UsersRoles
import org.careerseekers.cseventsservice.enums.VerificationStatus
import org.careerseekers.cseventsservice.mocks.generators.MocksGenerator.randomBoolean
import org.careerseekers.cseventsservice.mocks.generators.MocksGenerator.randomDateOfBirth
import org.careerseekers.cseventsservice.mocks.generators.MocksGenerator.randomEmail
import org.careerseekers.cseventsservice.mocks.generators.MocksGenerator.randomString
import kotlin.random.Random.Default.nextLong

object CachedUsersGenerator {
    val roles = listOf(UsersRoles.USER, UsersRoles.ADMIN, UsersRoles.EXPERT, UsersRoles.TUTOR, UsersRoles.MENTOR)

    fun createUser() = UsersCacheDto(
        id = nextLong(1, 100000),
        firstName = randomString(12),
        lastName = randomString(12),
        patronymic = randomString(12),
        dateOfBirth = randomDateOfBirth(),
        email = randomEmail(),
        mobileNumber = "+7" + nextLong(1000000000, 9999999999),
        password = randomString(12),
        role = roles.random(),
        avatarId = nextLong(1, 100000),
        verified = VerificationStatus.UNCHECKED,
        isMentor = randomBoolean(),
    )
}