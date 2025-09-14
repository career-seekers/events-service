package org.careerseekers.cseventsservice.dto.directions

import org.careerseekers.cseventsservice.dto.DtoClass
import org.careerseekers.cseventsservice.enums.DirectionAgeCategory
import org.springframework.web.multipart.MultipartFile

data class CreateDirectionDto(
    val name: String,
    val description: String,
    val ageCategory: List<DirectionAgeCategory>,
    val icon: MultipartFile? = null,
    val iconId: Long? = null,
    val userId: Long? = null,
    val expertId: Long? = null,
) : DtoClass
