package org.careerseekers.cseventsservice.dto.rapports

import org.careerseekers.cseventsservice.dto.DtoClass

data class ChildRecordsRapportDto(
    val childName: String,
    val parentName: String,
    val email: String,
    val mobileNumber: String,
    val tgLink: String,
    val schoolName: String,
    val trainingGroundName: String,
    val queueStatus: String,
    val ageCategory: String,
) : DtoClass
