package org.careerseekers.cseventsservice.mappers

import org.careerseekers.cseventsservice.dto.events.CreateEventDto
import org.careerseekers.cseventsservice.entities.DirectionAgeCategories
import org.careerseekers.cseventsservice.entities.Directions
import org.careerseekers.cseventsservice.entities.Events
import org.careerseekers.cseventsservice.enums.VerificationStatus
import org.mapstruct.Mapper
import org.mapstruct.Mapping

@Mapper(componentModel = "spring")
interface EventsMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "name", source = "dto.name")
    @Mapping(target = "description", source = "dto.description")
    @Mapping(target = "direction", source = "direction")
    @Mapping(target = "directionAgeCategory", source = "directionAgeCategory")
    @Mapping(target = "verified", source = "verificationStatus")
    fun eventFromDto(
        dto: CreateEventDto,
        direction: Directions,
        directionAgeCategory: DirectionAgeCategories,
        verificationStatus: VerificationStatus
    ): Events
}