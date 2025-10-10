package org.careerseekers.cseventsservice.entities

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.FetchType
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.JoinColumn
import jakarta.persistence.ManyToOne
import jakarta.persistence.Table
import jakarta.persistence.Version
import org.careerseekers.cseventsservice.enums.ParticipantStatus
import org.careerseekers.cseventsservice.enums.QueueStatus
import org.careerseekers.cseventsservice.io.converters.ConvertableToHttpResponse
import java.util.Date

@Entity
@Table(name = "child_to_direction")
data class ChildToDirection(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long,

    @Column(nullable = false)
    var childId: Long,

    @Column(nullable = false)
    var status: ParticipantStatus,

    @Column(nullable = false)
    var queueStatus: QueueStatus,

    @Column(nullable = true)
    var teacherName: String?,

    @Column(nullable = true)
    var institution: String?,

    @Column(nullable = true)
    var post: String?,

    @Column(nullable = false)
    var createdAt: Date,

    @Version
    var version: Int? = null,

    @JsonIgnoreProperties(value = ["documents", "participants", "ageCategories"])
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "direction_id", nullable = false)
    var direction: Directions,

    @JsonIgnoreProperties(value = ["participants", "direction"])
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "direction_age_category_id", nullable = false)
    var directionAgeCategory: DirectionAgeCategories,
) : ConvertableToHttpResponse<ChildToDirection>