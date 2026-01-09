package org.careerseekers.cseventsservice.aspects

import org.aspectj.lang.JoinPoint
import org.aspectj.lang.annotation.AfterReturning
import org.aspectj.lang.annotation.Aspect
import org.careerseekers.cseventsservice.clients.GraphQlUsersServiceClient
import org.careerseekers.cseventsservice.dto.EventCreationDto
import org.careerseekers.cseventsservice.dto.events.UpdateEventVerificationDto
import org.careerseekers.cseventsservice.enums.DirectionAgeCategory.Companion.getAgeAlias
import org.careerseekers.cseventsservice.repositories.EventsRepository
import org.careerseekers.cseventsservice.services.kafka.producers.EventCreationProducer
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Component

@Aspect
@Component
class EventVerificationAspect(
    private val eventCreationProducer: EventCreationProducer,
    private val eventsRepository: EventsRepository,
    private val usersServiceClient: GraphQlUsersServiceClient
) {

    private val logger = LoggerFactory.getLogger(this.javaClass)

    @AfterReturning(value = "@annotation(org.careerseekers.cseventsservice.annotations.EventVerification)")
    fun afterReturning(joinPoint: JoinPoint) {
        val args = joinPoint.args
        if (args.isNotEmpty()) {
            val dto = args[0] as UpdateEventVerificationDto
            if (dto.verified) {
                val event = eventsRepository.findById(dto.id).orElse(null) ?: return
                val expert = event.direction.expertId?.let { usersServiceClient.getUserById(it) }

                logger.info("Sending EventCreation message for verified event ID: ${dto.id}")

                eventCreationProducer.sendMessage(
                    EventCreationDto(
                        eventType = event.eventType,
                        directionName = event.direction.name,
                        ageCategory = event.directionAgeCategory.ageCategory.getAgeAlias(),
                        expertName = expert?.getFullName() ?: "",
                        expertEmail = expert?.email ?: "",
                        participantsList = event.direction.participants?.map { it.id } ?: emptyList(),
                    )
                )
            }
        }
    }
}