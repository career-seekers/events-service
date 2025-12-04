package org.careerseekers.cseventsservice.utils.timefold

import ai.timefold.solver.core.api.domain.solution.PlanningEntityCollectionProperty
import ai.timefold.solver.core.api.domain.solution.PlanningScore
import ai.timefold.solver.core.api.domain.solution.PlanningSolution
import ai.timefold.solver.core.api.domain.solution.ProblemFactCollectionProperty
import ai.timefold.solver.core.api.domain.valuerange.ValueRangeProvider
import ai.timefold.solver.core.api.score.buildin.hardsoft.HardSoftScore

@PlanningSolution
data class ChildrenSchedule(

    @ValueRangeProvider(id = "slotRange")
    @ProblemFactCollectionProperty
    val slots: List<Slot> = emptyList(),

    @PlanningEntityCollectionProperty
    val assignments: List<ChildDirectionAssignment> = emptyList(),

    @PlanningScore
    var score: HardSoftScore? = null
)
