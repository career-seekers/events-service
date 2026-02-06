package org.careerseekers.cseventsservice.dto.events

import org.careerseekers.cseventsservice.dto.DtoClass
import org.careerseekers.cseventsservice.dto.FilterDtoClass
import org.careerseekers.cseventsservice.enums.DirectionAgeCategory
import org.careerseekers.cseventsservice.enums.EventFormats
import org.careerseekers.cseventsservice.enums.EventTypes
import org.careerseekers.cseventsservice.enums.VerificationStatus
import java.time.ZonedDateTime

data class EventsFilterDto(
    val name: String?,
    val eventType: EventTypes?,
    val eventFormat: EventFormats?,
    val verified: VerificationStatus?,
    val startDateTime: ZonedDateTime?,
    val endDateTime: ZonedDateTime?,
    val directionName: String?,
    val ageCategory: DirectionAgeCategory?,
    val relatedUserId: Long?,
    val isDraft: Boolean?,
) : DtoClass, FilterDtoClass