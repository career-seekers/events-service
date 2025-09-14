package org.careerseekers.cseventsservice.entities

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

    @Column(nullable = false)
    var participantsCount: Long = 0L,

    @JsonIgnoreProperties(value = ["direction"])
    @OneToMany(mappedBy = "direction", cascade = [CascadeType.ALL], orphanRemoval = true)
    var ageCategories: MutableList<DirectionAgeCategories>? = mutableListOf(),

    @JsonIgnoreProperties(value = ["direction"])
    @OneToMany(mappedBy = "direction", cascade = [CascadeType.ALL], orphanRemoval = true)
    val documents: MutableList<DirectionDocuments>? = mutableListOf(),
) : ConvertableToHttpResponse<Directions>