package org.careerseekers.cseventsservice.dto.events

import org.careerseekers.cseventsservice.dto.DtoClass
import org.careerseekers.cseventsservice.enums.EventFormats
import org.careerseekers.cseventsservice.enums.EventTypes
import java.time.ZonedDateTime

data class CreateEventDto(
    val name: String,
    val shortDescription: String,
    val eventType: EventTypes,
    val eventFormat: EventFormats,
    val startDateTime: ZonedDateTime,
    val endDateTime: ZonedDateTime,
    val eventVenue: String?,
    val description: String?,
    val directionId: Long,
    val directionAgeCategoryId: Long,
    val createdAt: ZonedDateTime? = ZonedDateTime.now(),
    val updatedAt: ZonedDateTime? = ZonedDateTime.now(),
) : DtoClass
