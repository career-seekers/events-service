package org.careerseekers.cseventsservice.entities

import com.fasterxml.jackson.annotation.JsonIgnore
import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import jakarta.persistence.CascadeType
import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.OneToMany
import jakarta.persistence.Table
import org.careerseekers.cseventsservice.io.converters.ConvertableToHttpResponse

@Entity
@Table(name = "directions")
data class Directions(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long,

    @Column(nullable = false)
    var name: String,

    @Column(nullable = false, columnDefinition = "TEXT")
    var description: String,

    @Column(nullable = true)
    var iconId: Long? = null,

    @Column(nullable = true)
    var userId: Long? = null,

    @Column(nullable = true)
    var expertId: Long? = null,

    @Transient
    private var _participantsCount: Long = 0L,

    @JsonIgnoreProperties(value = ["direction", "participants"])
    @OneToMany(mappedBy = "direction", cascade = [CascadeType.ALL], orphanRemoval = true)
    var ageCategories: MutableList<DirectionAgeCategories>? = mutableListOf(),

    @JsonIgnoreProperties(value = ["direction"])
    @OneToMany(mappedBy = "direction", cascade = [CascadeType.ALL], orphanRemoval = true)
    val documents: MutableList<DirectionDocuments>? = mutableListOf(),

    @JsonIgnore
    @OneToMany(mappedBy = "direction", cascade = [CascadeType.ALL], orphanRemoval = true)
    val participants: MutableList<ChildToDirection>? = mutableListOf(),

    @JsonIgnore
    @OneToMany(mappedBy = "direction", cascade = [CascadeType.ALL], orphanRemoval = true)
    val events: MutableList<Events>? = mutableListOf(),
) : ConvertableToHttpResponse<Directions> {

    val participantsCount: Long
        get() = ageCategories?.sumOf { it.currentParticipantsCount } ?: 0L

    val eventsCount: Int
        get() = events?.size ?: 0
}