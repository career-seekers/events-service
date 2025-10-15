package org.careerseekers.cseventsservice.dto.directions

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import org.careerseekers.cseventsservice.dto.DtoClass
import org.careerseekers.cseventsservice.entities.DirectionAgeCategories

data class DirectionsOutputDto(
    val id: Long,
    val name: String,
    val description: String,
    val userId: Long,
    val expertId: Long,
    @JsonIgnoreProperties(value = ["participants", "direction"])
    val ageCategories: List<DirectionAgeCategories>
) : DtoClass