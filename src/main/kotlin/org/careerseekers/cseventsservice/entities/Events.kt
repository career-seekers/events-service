package org.careerseekers.cseventsservice.entities

import com.fasterxml.jackson.annotation.JsonIgnore
import jakarta.persistence.*
import org.careerseekers.cseventsservice.enums.DirectionAgeCategory
import org.careerseekers.cseventsservice.enums.EventFormats
import org.careerseekers.cseventsservice.enums.EventTypes
import java.time.ZonedDateTime

@Entity
@Table(name = "events")
data class Events(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long,

    @Column(nullable = false)
    var name: String,

    @Column(nullable = false)
    var shortDescription: String,

    @Column(nullable = false)
    var eventType: EventTypes,

    @Column(nullable = false)
    var eventFormat: EventFormats,

    @Column(nullable = false)
    var startDateTime: ZonedDateTime,

    @Column(nullable = false)
    var endDateTime: ZonedDateTime,

    @Column(nullable = true, columnDefinition = "TEXT")
    var eventVenue: String?,

    @Column(nullable = true, columnDefinition = "TEXT")
    var description: String?,

    @JsonIgnore
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "direction_id", nullable = false)
    var direction: Directions,

    @JsonIgnore
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "direction_age_category_id", nullable = false)
    var directionAgeCategory: DirectionAgeCategories,

    @Version
    var version: Int? = null,
) {
    val directionName: String
        get() = direction.name

    val directionAgeCategoryName: DirectionAgeCategory
        get() = directionAgeCategory.ageCategory
}