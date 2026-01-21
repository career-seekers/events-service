package org.careerseekers.cseventsservice.entities

import com.fasterxml.jackson.annotation.JsonIgnore
import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import jakarta.persistence.*
import jakarta.validation.constraints.Min
import org.careerseekers.cseventsservice.enums.DirectionAgeCategory
import kotlin.jvm.Transient

@Entity
@Table(name = "direction_age_categories")
data class DirectionAgeCategories(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long,

    @Column(nullable = false)
    var ageCategory: DirectionAgeCategory,

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "direction_id", nullable = false)
    var direction: Directions,

    @field:Min(0)
    @Column(nullable = false)
    var maxParticipantsCount: Long = 0L,

    @Column(nullable = false)
    var isDisabled: Boolean = false,

    @Transient
    private var _currentParticipantsCount: Long = 0L,

    @JsonIgnoreProperties(value = ["directionAgeCategory"])
    @OneToMany(mappedBy = "directionAgeCategory", cascade = [CascadeType.ALL], orphanRemoval = true)
    val participants: MutableList<ChildToDirection>? = mutableListOf(),

    @JsonIgnore
    @OneToMany(mappedBy = "directionAgeCategory", cascade = [CascadeType.ALL], orphanRemoval = true)
    val events: MutableList<Events>? = mutableListOf(),
) {
    val currentParticipantsCount: Long
        get() = participants?.size?.toLong() ?: 0L

    val eventsCount: Int
        get() = events?.size ?: 0
}
