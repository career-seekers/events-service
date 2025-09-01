package org.careerseekers.cseventsservice.io.converters.extensions

import org.careerseekers.cseventsservice.dto.PlatformDto
import org.careerseekers.cseventsservice.entities.Platforms

fun Platforms.toKafkaPlatformDto() = PlatformDto(
    id = this.id,
    fullName = this.fullName,
    shortName = this.shortName,
    address = this.address,
    userId = this.userId,
)