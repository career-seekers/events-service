package org.careerseekers.cseventsservice.utils.timefold

import ai.timefold.solver.core.api.score.buildin.hardsoft.HardSoftScore
import ai.timefold.solver.core.api.score.stream.Constraint
import ai.timefold.solver.core.api.score.stream.ConstraintCollectors
import ai.timefold.solver.core.api.score.stream.ConstraintFactory
import ai.timefold.solver.core.api.score.stream.ConstraintProvider
import ai.timefold.solver.core.api.score.stream.Joiners
import org.springframework.stereotype.Component

@Component
class ChildrenScheduleConstraintProvider : ConstraintProvider {
    override fun defineConstraints(factory: ConstraintFactory): Array<Constraint> {
        return arrayOf(
            slotCapacity(factory),
            matchingDirectionAndAge(factory),
            oneFlowPerChild(factory),
            unassignedPenalty(factory),
        )
    }

    private fun slotCapacity(factory: ConstraintFactory): Constraint =
        factory
            .forEach(ChildDirectionAssignment::class.java)
            .filter { assignment -> assignment.slot != null }
            .groupBy(
                { assignment -> assignment.slot!! },
                ConstraintCollectors.toList()
            )
            .filter { slot, assignments ->
                assignments.size > slot.capacity
            }
            .penalize(HardSoftScore.ONE_HARD) { slot, assignments ->
                assignments.size - slot.capacity
            }
            .asConstraint("Slot capacity exceeded")


    private fun matchingDirectionAndAge(factory: ConstraintFactory): Constraint =
        factory.forEach(ChildDirectionAssignment::class.java)
            .filter { it.slot != null }
            .filter { a ->
                a.slot!!.directionId != a.directionId ||
                        a.slot!!.ageCategoryId != a.ageCategoryId
            }
            .penalize(HardSoftScore.ONE_HARD)
            .asConstraint("Slot direction or age mismatch")

    private fun unassignedPenalty(factory: ConstraintFactory): Constraint =
        factory.forEach(ChildDirectionAssignment::class.java)
            .filter { it.slot == null }
            .penalize(HardSoftScore.ONE_SOFT)
            .asConstraint("Unassigned child direction")

    private fun oneFlowPerChild(factory: ConstraintFactory): Constraint =
        factory
            .forEachUniquePair(
                ChildDirectionAssignment::class.java,
                Joiners.equal({ a -> a.childId }, { b -> b.childId }),
                Joiners.equal({ a -> a.slot?.flow }, { b -> b.slot?.flow }),
            )
            .filter { a, b -> a.slot != null && b.slot != null }
            .penalize(HardSoftScore.ONE_HARD)
            .asConstraint("Child in two assignments with same flow")
}