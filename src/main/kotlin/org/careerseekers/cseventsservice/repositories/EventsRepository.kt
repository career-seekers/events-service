package org.careerseekers.cseventsservice.repositories

import org.careerseekers.cseventsservice.entities.Events
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.JpaSpecificationExecutor
import org.springframework.data.jpa.repository.Query
import org.springframework.stereotype.Repository

@Repository
interface EventsRepository : JpaRepository<Events, Long>, JpaSpecificationExecutor<Events> {

    @Query("SELECT e FROM Events e WHERE e.direction.id = :id")
    fun findByDirectionId(id: Long): List<Events>

    @Query("SELECT e FROM Events e WHERE e.directionAgeCategory.id = :id")
    fun findByDirectionAgeCategoryId(id: Long): List<Events>
}