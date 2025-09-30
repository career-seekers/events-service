package org.careerseekers.cseventsservice.dto.directions.childToDirection

import org.careerseekers.cseventsservice.dto.DtoClass
import org.careerseekers.cseventsservice.enums.ParticipantStatus
import org.careerseekers.cseventsservice.enums.QueueStatus

data class UpdateChildToDirectionsDto(
    val id: Long,
    var status: ParticipantStatus?,
    var queueStatus: QueueStatus?,
) : DtoClass
