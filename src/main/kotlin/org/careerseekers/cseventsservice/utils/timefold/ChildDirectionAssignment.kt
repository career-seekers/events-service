package org.careerseekers.cseventsservice.utils.timefold

import ai.timefold.solver.core.api.domain.entity.PlanningEntity
import ai.timefold.solver.core.api.domain.lookup.PlanningId
import ai.timefold.solver.core.api.domain.variable.PlanningVariable

@PlanningEntity
data class ChildDirectionAssignment(
    @PlanningId
    val id: Long? = null ,

    var childId: Long? = null,
    var directionId: Long? = null,
    var ageCategoryId: Long? = null,

    @PlanningVariable(valueRangeProviderRefs = ["slotRange"])
    var slot: Slot? = null,
)