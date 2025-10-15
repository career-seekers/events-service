package org.careerseekers.cseventsservice.aspects.interfaces

import org.aspectj.lang.JoinPoint

interface IEntityUpdatesAspect {
    fun afterUpdate(joinPoint: JoinPoint)
}