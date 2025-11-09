package org.careerseekers.cseventsservice.dto.events

import org.careerseekers.cseventsservice.dto.DtoClass

data class UpdateEventVerificationDto(
    val id: Long,
    val verified: Boolean,
) : DtoClass
