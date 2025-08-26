package org.careerseekers.cseventsservice.dto.platforms

import org.careerseekers.cseventsservice.dto.DtoClass

data class CreatePlatformDto(
    val fullName: String,
    val shortName: String,
    val address: String,
    val verified: Boolean? = false,
    val userId: Long,
) : DtoClass