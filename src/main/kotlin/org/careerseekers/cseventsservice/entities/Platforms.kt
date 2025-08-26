package org.careerseekers.cseventsservice.entities

import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.Table
import org.careerseekers.cseventsservice.io.converters.ConvertableToHttpResponse

@Entity
@Table(name = "platforms")
data class Platforms(

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long,

    @Column(nullable = false)
    var fullName: String,

    @Column(nullable = false)
    var shortName: String,

    @Column(nullable = false)
    var address: String,

    @Column(nullable = false)
    var verified: Boolean = false,

    @Column(nullable = false)
    var userId: Long? = null,
) : ConvertableToHttpResponse<Platforms>