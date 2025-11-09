package org.careerseekers.cseventsservice.dto.events

import org.careerseekers.cseventsservice.dto.DtoClass
import org.careerseekers.cseventsservice.enums.EventFormats
import org.careerseekers.cseventsservice.enums.EventTypes
import java.time.ZonedDateTime

data class UpdateEventDto(
    val id: Long,
    val name: String?,
    val shortDescription: String?,
    val eventType: EventTypes?,
    val eventFormats: EventFormats?,
    val startDateTime: ZonedDateTime?,
    val endDateTime: ZonedDateTime?,
    val eventVenue: String?,
    val description: String?,
    val directionAgeCategoryId: Long?
) : DtoClass
