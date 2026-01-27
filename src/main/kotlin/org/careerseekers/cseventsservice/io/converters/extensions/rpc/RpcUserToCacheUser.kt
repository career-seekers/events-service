package org.careerseekers.cseventsservice.io.converters.extensions.rpc

import com.careerseekers.grpc.users.User
import org.careerseekers.cseventsservice.dto.UsersCacheDto
import org.careerseekers.cseventsservice.enums.UsersRoles
import org.careerseekers.cseventsservice.enums.VerificationStatus
import org.careerseekers.cseventsservice.io.converters.extensions.toDate

fun User.toCache() : UsersCacheDto {
    return UsersCacheDto(
        id = this.id,
        firstName = this.firstName,
        lastName = this.lastName,
        patronymic = this.patronymic,
        dateOfBirth = this.dateOfBirth.toDate(),
        email = this.email,
        mobileNumber = this.mobileNumber,
        password = this.password,
        role = UsersRoles.valueOf(this.role),
        avatarId = this.avatarId,
        verified = VerificationStatus.valueOf(this.verified.name),
        isMentor = this.isMentor,
    )
}