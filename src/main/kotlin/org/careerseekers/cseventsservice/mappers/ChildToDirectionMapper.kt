package org.careerseekers.cseventsservice.mappers

import org.careerseekers.cseventsservice.dto.directions.childToDirection.CreateChildWithDirectionDto
import org.careerseekers.cseventsservice.entities.ChildToDirection
import org.mapstruct.Mapper

@Mapper(componentModel = "spring")
interface ChildToDirectionMapper {
    fun objectFromDto(dto: CreateChildWithDirectionDto): ChildToDirection
}