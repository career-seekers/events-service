package org.careerseekers.cseventsservice.dto.directions.childToDirection

import org.careerseekers.cseventsservice.dto.DtoClass

data class SetTeacherInfoDto(
    val id: Long,
    val teacherName: String,
    val institution: String,
    val post: String,
) : DtoClass
