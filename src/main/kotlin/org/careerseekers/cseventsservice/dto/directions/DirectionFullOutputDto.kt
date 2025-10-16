package org.careerseekers.cseventsservice.dto.directions

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import org.careerseekers.cseventsservice.dto.DtoClass
import org.careerseekers.cseventsservice.entities.DirectionAgeCategories
import org.careerseekers.cseventsservice.entities.DirectionDocuments

data class DirectionFullOutputDto(
    val id: Long,
    val name: String,
    val description: String,
    val userId: Long,
    val expertId: Long,
    @JsonIgnoreProperties(value = ["participants", "direction"])
    val ageCategories: List<DirectionAgeCategories>,
    val documents: List<DirectionDocuments>,
    val participantsCount: Long
) : DtoClass
