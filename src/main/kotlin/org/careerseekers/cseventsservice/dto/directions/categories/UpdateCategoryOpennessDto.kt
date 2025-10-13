package org.careerseekers.cseventsservice.dto.directions.categories

import org.careerseekers.cseventsservice.dto.DtoClass

data class UpdateCategoryOpennessDto(
    val id: Long,
    val status: Boolean
) : DtoClass
