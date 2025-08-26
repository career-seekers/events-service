package org.careerseekers.cseventsservice.dto.platforms

import org.careerseekers.cseventsservice.dto.DtoClass

data class ChangePlatformOwnerDto(
    val id: Long,
    val userId: Long,
) : DtoClass