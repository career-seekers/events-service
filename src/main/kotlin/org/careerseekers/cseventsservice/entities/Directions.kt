package org.careerseekers.cseventsservice.entities

import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.Table
import org.careerseekers.cseventsservice.enums.DirectionAgeCategory
import org.careerseekers.cseventsservice.io.converters.ConvertableToHttpResponse

@Entity
@Table(name = "directions")
data class Directions(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long,

    @Column(nullable = false)
    var name: String,

    @Column(nullable = false)
    var description: String,

    @Column(nullable = false)
    var ageCategory: DirectionAgeCategory,

    @Column(nullable = true)
    var iconId: Long? = null,

    @Column(nullable = true)
    var userId: Long? = null,
) : ConvertableToHttpResponse<Directions>