package org.careerseekers.cseventsservice.utils

import org.careerseekers.cseventsservice.services.ChildToDirectionService
import org.springframework.stereotype.Component

@Component
class ChildrenBalancer(
    private val childToDirectionService: ChildToDirectionService,
) {
    data class Slot(
        val directionId: Long,
        val ageCategoryId: Long,
        val capacity: Int,
        val kids: MutableList<Long>,
        val flow: Int,
    )

    private data class RejectedChild(val id: Long, val directionId: Long, val ageCategoryId: Long)

    private val slots = mutableListOf(
        // PRESCHOOL 1
        // лабораторный анализ
        Slot(19, 79, 4, mutableListOf(), 1),
        Slot(19, 79, 4, mutableListOf(), 2),
        Slot(19, 79, 4, mutableListOf(), 3),

        // физик исследователь
        Slot(110, 341, 14, mutableListOf(), 0),
        Slot(110, 341, 14, mutableListOf(), 0),
        Slot(110, 341, 0, mutableListOf(), 0),

        // киберспорт
        Slot(74, 278, 10, mutableListOf(), 1),
        Slot(74, 278, 9, mutableListOf(), 2),
        Slot(74, 278, 0, mutableListOf(), 3),

        // мобильная рб
        Slot(75, 138, 8, mutableListOf(), 1),
        Slot(75, 138, 8, mutableListOf(), 2),
        Slot(75, 138, 0, mutableListOf(), 3),

        // электромонтаж
        Slot(52, 231, 0, mutableListOf(), 1),
        Slot(52, 231, 5, mutableListOf(), 2),
        Slot(52, 231, 5, mutableListOf(), 3),

        // актер театра
        Slot(101, 256, 19, mutableListOf(), 0),
        Slot(101, 256, 19, mutableListOf(), 1),
        Slot(101, 256, 0, mutableListOf(), 2),

        // видеопроизводство
        Slot(70, 125, 5, mutableListOf(), 1),
        Slot(70, 125, 5, mutableListOf(), 2),


        // PRESCHOOL 2
        // электроника
        Slot(55, 310, 6, mutableListOf(), 1),
        Slot(55, 310, 6, mutableListOf(), 2),
        Slot(55, 310, 0, mutableListOf(), 3),

        // киберспорт
        Slot(80, 275, 8, mutableListOf(), 1),
        Slot(80, 275, 0, mutableListOf(), 2),
        Slot(80, 275, 0, mutableListOf(), 3),

        // физик исследователь
        Slot(27, 78, 0, mutableListOf(), 0),
        Slot(27, 78, 0, mutableListOf(), 0),
        Slot(27, 78, 17, mutableListOf(), 0),

        // мобильная рб
        Slot(75, 139, 5, mutableListOf(), 2),
        Slot(75, 139, 5, mutableListOf(), 3),
        Slot(75, 139, 5, mutableListOf(), 4),


        // графический дизайн
        Slot(56, 260, 4, mutableListOf(), 1),
        Slot(56, 260, 0, mutableListOf(), 2),
        Slot(56, 260, 0, mutableListOf(), 3),

        // электромонтаж
        Slot(52, 232, 5, mutableListOf(), 1),
        Slot(52, 232, 5, mutableListOf(), 2),
        Slot(52, 232, 0, mutableListOf(), 3),

        // кастомизация
        Slot(73, 131, 6, mutableListOf(), 1),
        Slot(73, 131, 0, mutableListOf(), 2),
        Slot(73, 131, 0, mutableListOf(), 3),

        // подводная рб
        Slot(94, 385, 6, mutableListOf(), 1),
        Slot(94, 385, 0, mutableListOf(), 2),
        Slot(94, 385, 0, mutableListOf(), 3),

        // инженер эколог
        Slot(93, 208, 4, mutableListOf(), 1),
        Slot(93, 208, 4, mutableListOf(), 2),
        Slot(93, 208, 4, mutableListOf(), 3),

        // актер театра
        Slot(102, 255, 14, mutableListOf(), 1),
        Slot(102, 255, 15, mutableListOf(), 2),
        Slot(102, 255, 0, mutableListOf(), 3),

        // мультипликация
        Slot(120, 308, 4, mutableListOf(), 1),
        Slot(120, 308, 4, mutableListOf(), 2),
        Slot(120, 308, 0, mutableListOf(), 3),

        // видеопроизводство
        Slot(70, 126, 5, mutableListOf(), 1),
        Slot(70, 126, 5, mutableListOf(), 2),
        Slot(70, 126, 0, mutableListOf(), 3),

        // декоратор
        Slot(103, 261, 18, mutableListOf(), 1),
        Slot(103, 261, 0, mutableListOf(), 2),
        Slot(103, 261, 0, mutableListOf(), 3),

        // архитектура
        Slot(119, 376, 8, mutableListOf(), 1),
        Slot(119, 376, 8, mutableListOf(), 2),
        Slot(119, 376, 0, mutableListOf(), 3),
    )

    fun balance(): MutableMap<Long, MutableList<Slot>> {
        val usedDirectionIds: Set<Long> = slots.map { it.directionId }.toSet()
        val usedAgeCategoryIds: Set<Long> = slots.map { it.ageCategoryId }.toSet()
        val rejectedChildren = mutableListOf<RejectedChild>()

        val allChildren = childToDirectionService.getAll()
            .filter { it.direction.id in usedDirectionIds }
            .filter { it.directionAgeCategory.id in usedAgeCategoryIds }

        val childrenToSlots = mutableMapOf<Long, MutableList<Slot>>()

        for (kid in allChildren) {
            val chosenSlots = mutableListOf<Slot>()
            val usedFlows = mutableSetOf<Int>()

            val availableSlots = slots
                .filter { it.directionId == kid.direction.id && it.ageCategoryId == kid.directionAgeCategory.id }

            for (professionSlot in availableSlots.filter { it.kids.size < it.capacity }) {
                if (professionSlot.flow !in usedFlows) {
                    professionSlot.kids.add(kid.childId)
                    chosenSlots.add(professionSlot)
                    usedFlows.add(professionSlot.flow)
                    break
                }
            }

            if (chosenSlots.isEmpty()) {
                rejectedChildren.add(RejectedChild(kid.childId, kid.direction.id, kid.directionAgeCategory.id))
            }

            childrenToSlots[kid.childId] = chosenSlots
        }

        rejectedChildren
            .sortedBy { it.directionId }
            .forEach { println(it) }

        return childrenToSlots
    }
}