package org.careerseekers.cseventsservice.dto.rapports

import org.careerseekers.cseventsservice.dto.DtoClass

data class ChildRecordsRapportDto(
    val childName: String,

    // Parent info
    val parentName: String,
    val email: String,
    val mobileNumber: String,
    val tgLink: String,

    // Mentor info
    val mentorName: String,
    val mentorEmail: String,
    val mentorPhoneNumber: String,
    val mentorTgLink: String,

    // School info
    val schoolName: String,
    val trainingGroundName: String,

    // Record info
    val queueStatus: String,
    val ageCategory: String,
) : DtoClass
