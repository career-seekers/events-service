package org.careerseekers.cseventsservice.utils.timefold

data class Slot(
    val id: Long,
    val directionId: Long,
    val ageCategoryId: Long,
    val capacity: Int,
    val flow: Int,
)