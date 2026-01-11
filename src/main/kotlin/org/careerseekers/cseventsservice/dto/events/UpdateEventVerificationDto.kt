package org.careerseekers.cseventsservice.dto.events

import org.careerseekers.cseventsservice.dto.DtoClass
import org.careerseekers.cseventsservice.enums.VerificationStatus

data class UpdateEventVerificationDto(
    val id: Long,
    val verified: VerificationStatus,
) : DtoClass
