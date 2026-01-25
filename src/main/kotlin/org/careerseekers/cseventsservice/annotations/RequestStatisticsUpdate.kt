package org.careerseekers.cseventsservice.annotations

import org.careerseekers.cseventsservice.enums.StatisticsUpdateRequestTypes

@Target(AnnotationTarget.FUNCTION)
@Retention(AnnotationRetention.RUNTIME)
annotation class RequestStatisticsUpdate(val type: StatisticsUpdateRequestTypes)