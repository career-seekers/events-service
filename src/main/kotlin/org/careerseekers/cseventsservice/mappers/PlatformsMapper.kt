package org.careerseekers.cseventsservice.mappers

import org.careerseekers.cseventsservice.dto.platforms.CreatePlatformDto
import org.careerseekers.cseventsservice.entities.Platforms
import org.mapstruct.Mapper

@Mapper(componentModel = "spring")
interface PlatformsMapper {
    fun platformFromDto(dto: CreatePlatformDto): Platforms
}