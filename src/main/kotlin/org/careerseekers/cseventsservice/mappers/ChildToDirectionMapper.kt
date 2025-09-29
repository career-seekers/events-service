package org.careerseekers.cseventsservice.mappers

import org.careerseekers.cseventsservice.dto.directions.childToDirection.LinkChildWithDirectionDto
import org.careerseekers.cseventsservice.entities.ChildToDirection
import org.mapstruct.Mapper

@Mapper(componentModel = "spring")
interface ChildToDirectionMapper {
    fun objectFromDto(dto: LinkChildWithDirectionDto): ChildToDirection
}