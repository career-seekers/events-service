package org.careerseekers.cseventsservice.dto.directions.childToDirection

import org.careerseekers.cseventsservice.dto.DtoClass
import org.careerseekers.cseventsservice.entities.DirectionAgeCategories
import org.careerseekers.cseventsservice.entities.Directions
import org.careerseekers.cseventsservice.enums.ParticipantStatus
import org.careerseekers.cseventsservice.enums.QueueStatus
import java.util.Date

data class CreateChildWithDirectionDto(
    val childId: Long,
    val status: ParticipantStatus? = ParticipantStatus.NOT_STATED,
    var queueStatus: QueueStatus? = null,
    var createdAt: Date? = Date(),
    val directionId: Long,
    var direction: Directions? = null,
    val directionAgeCategoryId: Long,
    var directionAgeCategory: DirectionAgeCategories? = null,
) : DtoClass
