package org.careerseekers.cseventsservice.mappers

import org.careerseekers.cseventsservice.dto.directions.categories.CreateAgeCategory
import org.careerseekers.cseventsservice.entities.DirectionAgeCategories
import org.mapstruct.Mapper

@Mapper(componentModel = "spring")
interface DirectionAgeCategoriesMapper {
    fun ageCategoryFromDto(o: CreateAgeCategory): DirectionAgeCategories
}