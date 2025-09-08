package org.careerseekers.cseventsservice.entities

import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.Table
import org.careerseekers.cseventsservice.enums.DirectionAgeCategory

@Entity
@Table(name = "directions")
data class Directions(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long,

    @Column(nullable = false)
    val name: String,

    @Column(nullable = false)
    val description: String,

    @Column(nullable = false)
    val ageCategory: DirectionAgeCategory,

    @Column(nullable = true)
    val iconId: Long? = null,

    @Column(nullable = true)
    val userId: Long? = null,
)