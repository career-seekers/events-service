package org.careerseekers.cseventsservice.dto.platforms

import org.careerseekers.cseventsservice.dto.DtoClass

data class UpdatePlatformDto(
    val id: Long,
    val fullName: String?,
    val shortName: String?,
    val address: String?,
) : DtoClass