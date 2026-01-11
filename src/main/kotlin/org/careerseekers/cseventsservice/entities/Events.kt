package org.careerseekers.cseventsservice.entities

import com.fasterxml.jackson.annotation.JsonIgnore
import jakarta.persistence.*
import org.careerseekers.cseventsservice.enums.DirectionAgeCategory.Companion.getAgeAlias
import org.careerseekers.cseventsservice.enums.EventFormats
import org.careerseekers.cseventsservice.enums.EventTypes
import org.careerseekers.cseventsservice.enums.VerificationStatus
import org.careerseekers.cseventsservice.io.converters.ConvertableToHttpResponse
import java.time.ZonedDateTime

@Entity
@Table(name = "events")
data class Events(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long?,

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

    @Column(nullable = true, columnDefinition = "TEXT")
    var eventVenue: String?,

    @Column(nullable = true, columnDefinition = "TEXT")
    var description: String?,

    @Column(nullable = false)
    var verificationStatus: VerificationStatus,

    @Column(nullable = true)
    var createdAt: ZonedDateTime?,

    @Column(nullable = true)
    var updatedAt: ZonedDateTime?,

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
) : ConvertableToHttpResponse<Events> {
    val directionId: Long
        get() = direction.id

    val directionAgeCategoryId: Long
        get() = directionAgeCategory.id

    val directionExpertId: Long
        get() = direction.expertId ?: 0

    val directionTutorId: Long
        get() = direction.userId ?: 0

    val directionName: String
        get() = direction.name

    val directionAgeCategoryName: String
        get() = directionAgeCategory.ageCategory.getAgeAlias()
}