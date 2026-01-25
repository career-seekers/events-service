package org.careerseekers.cseventsservice.aspects

import org.aspectj.lang.JoinPoint
import org.aspectj.lang.annotation.AfterReturning
import org.aspectj.lang.annotation.Aspect
import org.aspectj.lang.reflect.MethodSignature
import org.careerseekers.cseventsservice.annotations.RequestStatisticsUpdate
import org.careerseekers.cseventsservice.aspects.interfaces.IEntityUpdatesAspect
import org.careerseekers.cseventsservice.dto.StatisticsUpdateRequestDto
import org.careerseekers.cseventsservice.services.kafka.producers.StatisticsUpdateKafkaProducer
import org.springframework.stereotype.Component

@Aspect
@Component
class RequestStatisticsUpdateAspect(private val statisticsUpdateKafkaProducer: StatisticsUpdateKafkaProducer) :
    IEntityUpdatesAspect {

    @AfterReturning("@annotation(org.careerseekers.cseventsservice.annotations.RequestStatisticsUpdate)")
    override fun afterUpdate(joinPoint: JoinPoint) {
        val signature = joinPoint.signature as MethodSignature
        val method = signature.method

        val annotation = method.getAnnotation(RequestStatisticsUpdate::class.java)
        val type = annotation.type

        statisticsUpdateKafkaProducer.sendMessage(StatisticsUpdateRequestDto(type))
    }
}