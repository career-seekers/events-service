package org.careerseekers.cseventsservice.mappers

import org.careerseekers.cseventsservice.dto.directions.CreateDirectionDto
import org.careerseekers.cseventsservice.entities.Directions
import org.mapstruct.Mapper

@Mapper(componentModel = "spring")
interface DirectionsMapper {
    fun directionFromDto(o: CreateDirectionDto): Directions
}