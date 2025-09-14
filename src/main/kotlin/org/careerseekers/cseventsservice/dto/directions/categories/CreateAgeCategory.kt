package org.careerseekers.cseventsservice.dto.directions.categories

import org.careerseekers.cseventsservice.dto.DtoClass
import org.careerseekers.cseventsservice.entities.Directions
import org.careerseekers.cseventsservice.enums.DirectionAgeCategory

data class CreateAgeCategory(
    val ageCategory: DirectionAgeCategory,
    var direction: Directions? = null
) : DtoClass
