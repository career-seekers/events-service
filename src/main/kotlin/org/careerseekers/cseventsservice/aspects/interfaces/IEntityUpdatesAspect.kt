package org.careerseekers.cseventsservice.aspects.interfaces

import org.aspectj.lang.JoinPoint

fun interface IEntityUpdatesAspect {
    fun afterUpdate(joinPoint: JoinPoint)
}