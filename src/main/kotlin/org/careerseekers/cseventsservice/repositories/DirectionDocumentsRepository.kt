package org.careerseekers.cseventsservice.repositories

import org.careerseekers.cseventsservice.entities.DirectionDocuments
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface DirectionDocumentsRepository : JpaRepository<DirectionDocuments, Long> {
    fun findByUserId(userId: Long): List<DirectionDocuments>
}