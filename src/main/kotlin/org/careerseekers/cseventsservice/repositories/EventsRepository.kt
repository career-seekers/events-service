package org.careerseekers.cseventsservice.repositories

import org.careerseekers.cseventsservice.entities.Events
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.JpaSpecificationExecutor
import org.springframework.stereotype.Repository

@Repository
interface EventsRepository : JpaRepository<Events, Long>, JpaSpecificationExecutor<Events>