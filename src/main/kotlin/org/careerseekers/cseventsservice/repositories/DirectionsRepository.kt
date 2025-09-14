package org.careerseekers.cseventsservice.repositories

import org.careerseekers.cseventsservice.entities.Directions
import org.careerseekers.cseventsservice.enums.DirectionAgeCategory
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param
import org.springframework.stereotype.Repository

@Repository
interface DirectionsRepository : JpaRepository<Directions, Long> {
    fun findByUserId(userId: Long): List<Directions>
    fun findByExpertId(expertId: Long): List<Directions>

    @Query("select d from Directions d join d.ageCategories a where a.ageCategory = :ageCategory")
    fun findByAgeCategory(@Param("ageCategory") ageCategory: DirectionAgeCategory): List<Directions>
}