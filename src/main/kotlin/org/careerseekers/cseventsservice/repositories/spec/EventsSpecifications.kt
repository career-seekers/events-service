package org.careerseekers.cseventsservice.repositories.spec

import org.careerseekers.cseventsservice.entities.DirectionAgeCategories
import org.careerseekers.cseventsservice.entities.Directions
import org.careerseekers.cseventsservice.entities.Events
import org.careerseekers.cseventsservice.enums.DirectionAgeCategory
import org.careerseekers.cseventsservice.enums.EventFormats
import org.careerseekers.cseventsservice.enums.EventTypes
import org.springframework.data.jpa.domain.Specification
import java.time.ZonedDateTime

object EventsSpecifications {
    fun hasName(name: String?): Specification<Events>? = name?.let {
        Specification { root, _, cb -> cb.like(root.get("name"), "%$it%") }
    }

    fun hasEventType(eventType: EventTypes?): Specification<Events>? = eventType?.let {
        Specification { root, _, cb -> cb.equal(root.get<EventTypes>("eventType"), it) }
    }

    fun hasEventFormat(eventFormat: EventFormats?): Specification<Events>? = eventFormat?.let {
        Specification { root, _, cb -> cb.equal(root.get<EventFormats>("eventFormat"), it) }
    }

    fun hasVerified(verified: Boolean?): Specification<Events>? = verified?.let {
        Specification { root, _, cb -> cb.equal(root.get<EventFormats>("verified"), it) }
    }

    fun hasStartDateTimeAfter(startDate: ZonedDateTime?): Specification<Events>? = startDate?.let {
        Specification { root, _, cb -> cb.greaterThanOrEqualTo(root.get("startDateTime"), it) }
    }

    fun hasEndDateTimeBefore(endDate: ZonedDateTime?): Specification<Events>? = endDate?.let {
        Specification { root, _, cb -> cb.lessThanOrEqualTo(root.get("endDateTime"), it) }
    }

    fun hasDirectionName(directionName: String?): Specification<Events>? = directionName?.let {
        Specification { root, _, cb ->
            cb.like(root.get<Directions>("direction").get("name"), "%$it%")
        }
    }

    fun hasAgeCategoryName(ageCategoryName: DirectionAgeCategory?): Specification<Events>? = ageCategoryName?.let {
        Specification { root, _, cb ->
            cb.equal(root.get<Events>("directionAgeCategory").get<DirectionAgeCategories>("ageCategory"), "%$it%")
        }
    }
}