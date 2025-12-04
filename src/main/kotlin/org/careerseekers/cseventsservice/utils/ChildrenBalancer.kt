package org.careerseekers.cseventsservice.utils

import ai.timefold.solver.core.api.solver.SolverManager
import org.careerseekers.cseventsservice.enums.QueueStatus
import org.careerseekers.cseventsservice.services.ChildToDirectionService
import org.careerseekers.cseventsservice.services.reports.ChildrenDistributionReportService
import org.careerseekers.cseventsservice.utils.PredicateStorage.setSchedule
import org.careerseekers.cseventsservice.utils.timefold.ChildDirectionAssignment
import org.careerseekers.cseventsservice.utils.timefold.ChildrenSchedule
import org.careerseekers.cseventsservice.utils.timefold.Slot
import org.springframework.stereotype.Component

@Component
class ChildrenBalancer(
    private val childToDirectionService: ChildToDirectionService,
    private val solverManager: SolverManager<ChildrenSchedule, Long>,
    private val childrenDistributionReportService: ChildrenDistributionReportService
) {


    private val slots = mutableListOf(
        // PRESCHOOL 1
        // лабораторный анализ
        Slot(1, 19, 79, 4, 1),
        Slot(2, 19, 79, 4, 2),
        Slot(3, 19, 79, 4, 3),

        // физик исследователь
        Slot(4, 110, 341, 14, 0),
        Slot(5, 110, 341, 14, 1),
        Slot(6, 110, 341, 14, 2),

        // киберспорт
        Slot(7, 74, 278, 10, 1),
        Slot(8, 74, 278, 10, 2),
        Slot(9, 74, 278, 10, 3),

        // мобильная рб
        Slot(10, 75, 138, 8, 0),
        Slot(11, 75, 138, 8, 1),
        Slot(12, 75, 138, 8, 2),

        // электромонтаж
        Slot(13, 52, 231, 5, 1),
        Slot(14, 52, 231, 5, 2),
        Slot(15, 52, 231, 5, 3),

        // актер театра
        Slot(16, 101, 256, 20, 0),
        Slot(17, 101, 256, 20, 1),
        Slot(18, 101, 256, 20, 2),

        // видеопроизводство
        Slot(19, 70, 125, 5, 1),
        Slot(20, 70, 125, 5, 2),
        Slot(63, 70, 125, 5, 3),


        // PRESCHOOL 2
        // электроника взр
        Slot(21, 55, 310, 6, 1),
        Slot(22, 55, 310, 6, 2),
        Slot(23, 55, 310, 6, 3),

        // киберспорт взр
        Slot(24, 80, 275, 8, 1),
        Slot(25, 80, 275, 8, 2),
        Slot(26, 80, 275, 8, 3),

        // физик исследователь взр
        Slot(27, 27, 78, 17, 1),
        Slot(28, 27, 78, 17, 2),
        Slot(29, 27, 78, 17, 3),

        // мобильная рб взр
        Slot(30, 75, 139, 5, 3),
        Slot(31, 75, 139, 5, 4),
        Slot(32, 75, 139, 5, 5),


        // графический дизайн взр
        Slot(33, 56, 260, 4, 1),
        Slot(61, 56, 260, 4, 2),
        Slot(62, 56, 260, 4, 3),

        // электромонтаж взр
        Slot(34, 52, 232, 5, 1),
        Slot(35, 52, 232, 5, 2),
        Slot(36, 52, 232, 5, 3),

        // кастомизация взр
        Slot(37, 73, 131, 6, 1),
        Slot(38, 73, 131, 6, 2),
        Slot(39, 73, 131, 6, 3),

        // подводная рб взр
        Slot(40, 94, 385, 9, 1),
        Slot(41, 94, 385, 9, 2),
        Slot(42, 94, 385, 9, 3),

        // инженер эколог взр
        Slot(43, 93, 208, 4, 1),
        Slot(44, 93, 208, 4, 2),
        Slot(45, 93, 208, 4, 3),

        // актер театра взр
        Slot(46, 102, 255, 15, 1),
        Slot(47, 102, 255, 15, 2),
        Slot(48, 102, 255, 15, 3),

        // мультипликация взр
        Slot(49, 120, 308, 4, 1),
        Slot(50, 120, 308, 4, 2),
        Slot(51, 120, 308, 4, 3),

        // видеопроизводство взр
        Slot(52, 70, 126, 5, 4),
        Slot(53, 70, 126, 5, 5),
        Slot(54, 70, 126, 5, 6),

        // декоратор взр
        Slot(55, 103, 261, 18, 1),
        Slot(56, 103, 261, 18, 2),
        Slot(57, 103, 261, 18, 3),

        // архитектура взр
        Slot(58, 119, 376, 8, 1),
        Slot(59, 119, 376, 8, 2),
        Slot(60, 119, 376, 8, 3),
    )

    fun balance(): ChildrenSchedule {
        val usedDirectionIds: Set<Long> = slots.map { it.directionId }.toSet()
        val usedAgeCategoryIds: Set<Long> = slots.map { it.ageCategoryId }.toSet()


        val assignments = childToDirectionService.getAll()
            .filter { it.direction.id in usedDirectionIds }
            .filter { it.directionAgeCategory.id in usedAgeCategoryIds }
            .filter { it.queueStatus == QueueStatus.PARTICIPATES }
            .mapIndexed { index, record ->
                ChildDirectionAssignment(
                    id = index.toLong() * 1000 + record.direction.id,
                    childId = record.childId,
                    directionId = record.direction.id,
                    ageCategoryId = record.directionAgeCategory.id,
                    slot = null,
                )
            }

        val problem = ChildrenSchedule(
            slots = slots,
            assignments = assignments,
        )

        val problemId = 1L
        val solverJob = solverManager.solve(problemId, problem)
        val solution: ChildrenSchedule = solverJob.finalBestSolution

        setSchedule(solution)
        return solution
    }

    fun balanceToMap() {
        val solution = balance()

        println(solution.assignments
            .filter { it.slot != null }
            .groupBy(
                keySelector = { it.childId },
                valueTransform = { it.slot!! },
            )
        )

        childrenDistributionReportService.createReport(slots, solution.assignments)
    }

    fun debugCapacity() {
        val usedDirectionIds = slots.map { it.directionId }.toSet()
        val usedAgeCategoryIds = slots.map { it.ageCategoryId }.toSet()

        val assignments = childToDirectionService.getAll()
            .filter { it.direction.id in usedDirectionIds }
            .filter { it.directionAgeCategory.id in usedAgeCategoryIds }
            .filter { it.queueStatus == QueueStatus.PARTICIPATES }

        val capacityByKey = slots
            .groupBy { it.directionId to it.ageCategoryId }
            .mapValues { (_, list) -> list.sumOf { it.capacity } }

        val demandByKey = assignments
            .groupBy { it.direction.id to it.directionAgeCategory.id }
            .mapValues { (_, list) -> list.size }

        val rows = (capacityByKey.keys + demandByKey.keys).distinct()

        rows.forEach { (dirId, ageId) ->
            val cap = capacityByKey[dirId to ageId] ?: 0
            val dem = demandByKey[dirId to ageId] ?: 0
            println("dir=$dirId age=$ageId  capacity=$cap  demand=$dem")
        }
    }
}