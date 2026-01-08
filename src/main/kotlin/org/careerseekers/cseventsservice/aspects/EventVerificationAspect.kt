package org.careerseekers.cseventsservice.aspects

import org.aspectj.lang.JoinPoint
import org.aspectj.lang.annotation.AfterReturning
import org.aspectj.lang.annotation.Aspect
import org.careerseekers.cseventsservice.dto.EventCreationDto
import org.careerseekers.cseventsservice.dto.events.UpdateEventVerificationDto
import org.careerseekers.cseventsservice.repositories.EventsRepository
import org.careerseekers.cseventsservice.services.kafka.producers.EventCreationProducer
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Component

@Aspect
@Component
class EventVerificationAspect(
    private val eventCreationProducer: EventCreationProducer,
    private val eventsRepository: EventsRepository,
) {

    private val logger = LoggerFactory.getLogger(this.javaClass)

    @AfterReturning(
        value = "@annotation(org.careerseekers.cseventsservice.annotations.EventVerification)",
        returning = "result"
    )
    fun afterReturning(joinPoint: JoinPoint) {
        val args = joinPoint.args
        if (args.isNotEmpty()) {
            val dto = args[0] as UpdateEventVerificationDto
            val event = eventsRepository.findById(dto.id).orElse(null) ?: return
            logger.info("Sending EventCreation message for verified event ID: ${dto.id}")

            eventCreationProducer.sendMessage(
                EventCreationDto(
                    eventType = event.eventType,
                    directionId = event.direction.id,
                    participantsList = event.direction.participants?.map { it.id } ?: emptyList(),
                )
            )
        }
    }
}