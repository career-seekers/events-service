package org.careerseekers.cseventsservice.repositories

import org.careerseekers.cseventsservice.entities.DirectionAgeCategories
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface DirectionAgeCategoriesRepository : JpaRepository<DirectionAgeCategories, Long>