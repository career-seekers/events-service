package org.careerseekers.cseventsservice.dto.directions.childToDirection

import org.careerseekers.cseventsservice.dto.DtoClass
import org.careerseekers.cseventsservice.enums.ParticipantStatus

data class SetParticipantStatus(
    val id: Long,
    val status: ParticipantStatus,
) : DtoClass
