package org.careerseekers.cseventsservice.utils

import org.careerseekers.cseventsservice.utils.timefold.ChildrenSchedule

object PredicateStorage {
    var schedule: ChildrenSchedule? = null
        private set

    fun setSchedule(schedule: ChildrenSchedule) = apply {this.schedule = schedule}
}