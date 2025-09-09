package org.careerseekers.cseventsservice.repositories

import org.careerseekers.cseventsservice.entities.Directions
import org.careerseekers.cseventsservice.enums.DirectionAgeCategory
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface DirectionsRepository : JpaRepository<Directions, Long> {
    fun findByUserId(userId: Long): List<Directions>
    fun findByExpertId(expertId: Long): List<Directions>
    fun findByAgeCategory(ageCategory: DirectionAgeCategory): List<Directions>
}