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
import org.careerseekers.cseventsservice.enums.DirectionAgeCategory
import org.careerseekers.cseventsservice.enums.FileTypes
import org.careerseekers.cseventsservice.io.converters.ConvertableToHttpResponse
import java.time.LocalDateTime

@Entity
@Table(name = "direction_documents")
data class DirectionDocuments(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long,

    @Column(nullable = false)
    var documentType: FileTypes,

    @Column(nullable = true)
    var ageCategory: DirectionAgeCategory? = null,

    @Column(nullable = false)
    var documentId: Long,

    @Column(nullable = true)
    var createdAt: LocalDateTime? = null,

    @Column(nullable = false)
    var verified: Boolean = false,

    @Column(nullable = true)
    var userId: Long? = null,

    @JsonIgnoreProperties(value = ["documents"])
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "direction_id")
    var direction: Directions? = null,
) : ConvertableToHttpResponse<DirectionDocuments>