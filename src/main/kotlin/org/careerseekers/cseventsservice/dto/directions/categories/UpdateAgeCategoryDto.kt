package org.careerseekers.cseventsservice.dto.directions.categories

import org.careerseekers.cseventsservice.dto.DtoClass

data class UpdateAgeCategoryDto(
    val id: Long,
    val maxParticipantsCount: Long?,
    val currentParticipantsCount: Long?,
) : DtoClass
