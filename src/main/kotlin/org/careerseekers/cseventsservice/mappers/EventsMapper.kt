package org.careerseekers.cseventsservice.mappers

import org.careerseekers.cseventsservice.dto.events.CreateEventDto
import org.careerseekers.cseventsservice.entities.DirectionAgeCategories
import org.careerseekers.cseventsservice.entities.Directions
import org.careerseekers.cseventsservice.entities.Events
import org.mapstruct.Mapper
import org.mapstruct.Mapping

@Mapper(componentModel = "spring")
interface EventsMapper {

    @Mapping(target = "direction", source = "direction")
    @Mapping(target = "directionAgeCategories", source = "directionAgeCategories")
    fun eventFromDto(dto: CreateEventDto, direction: Directions, directionAgeCategories: DirectionAgeCategories): Events
}