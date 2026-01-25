package org.careerseekers.cseventsservice.repositories

import org.careerseekers.cseventsservice.entities.Platforms
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface PlatformsRepository : JpaRepository<Platforms, Long> {
    fun findByUserId(userId: Long): Platforms?
    fun findByEmail(email: String): Platforms?
    fun countByVerified(verified: Boolean): Long
}