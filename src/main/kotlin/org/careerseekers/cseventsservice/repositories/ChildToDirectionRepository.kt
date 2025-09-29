package org.careerseekers.cseventsservice.repositories

import org.careerseekers.cseventsservice.entities.ChildToDirection
import org.springframework.data.jpa.repository.JpaRepository

interface ChildToDirectionRepository : JpaRepository<ChildToDirection, Long> {
    fun findByChildId(childId: Long): List<ChildToDirection>
    fun findByDirectionId(directionId: Long): List<ChildToDirection>
}