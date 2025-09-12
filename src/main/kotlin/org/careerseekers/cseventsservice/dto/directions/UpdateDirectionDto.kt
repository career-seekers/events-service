package org.careerseekers.cseventsservice.dto.directions

import org.careerseekers.cseventsservice.dto.DtoClass
import org.careerseekers.cseventsservice.enums.DirectionAgeCategory
import org.springframework.web.multipart.MultipartFile

data class UpdateDirectionDto(
    val id: Long,
    val name: String? = null,
    val description: String? = null,
    val ageCategory: DirectionAgeCategory? = null,
    val icon: MultipartFile? = null,
    val expertId: Long? = null,
) : DtoClass