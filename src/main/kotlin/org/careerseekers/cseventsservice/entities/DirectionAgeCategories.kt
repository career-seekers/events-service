package org.careerseekers.cseventsservice.entities

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import jakarta.persistence.CascadeType
import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.FetchType
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.JoinColumn
import jakarta.persistence.ManyToOne
import jakarta.persistence.OneToMany
import jakarta.persistence.Table
import jakarta.validation.constraints.Min
import org.careerseekers.cseventsservice.enums.DirectionAgeCategory

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

    @field:Min(0)
    @Column(nullable = false)
    var currentParticipantsCount: Long = 0L,

    @JsonIgnoreProperties(value = ["directionAgeCategory"])
    @OneToMany(mappedBy = "directionAgeCategory", cascade = [CascadeType.ALL], orphanRemoval = true)
    val participants: MutableList<ChildToDirection>? = mutableListOf(),
) {
    fun increaseCurrentParticipantsCount() {
        this.currentParticipantsCount += 1
    }

    fun decreaseCurrentParticipantsCount() {
        this.currentParticipantsCount -= 1
    }
}
