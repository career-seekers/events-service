package org.careerseekers.cseventsservice.aspects

import com.careerseekers.grpc.children.ChildrenServiceGrpc
import com.careerseekers.grpc.children.Id
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch
import net.devh.boot.grpc.client.inject.GrpcClient
import org.aspectj.lang.JoinPoint
import org.aspectj.lang.annotation.AfterReturning
import org.aspectj.lang.annotation.Aspect
import org.careerseekers.cseventsservice.aspects.interfaces.IEntityUpdatesAspect
import org.careerseekers.cseventsservice.dto.ParticipantStatusUpdate
import org.careerseekers.cseventsservice.dto.directions.childToDirection.UpdateChildToDirectionsDto
import org.careerseekers.cseventsservice.enums.DirectionAgeCategory.Companion.getAgeAlias
import org.careerseekers.cseventsservice.io.converters.extensions.rpc.toCache
import org.careerseekers.cseventsservice.services.ChildToDirectionService
import org.careerseekers.cseventsservice.services.kafka.producers.ParticipantStatusUpdateKafkaProducer
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Component

@Aspect
@Component
class ParticipantStatusUpdateAspect(
    private val childToDirectionService: ChildToDirectionService,
    private val kafkaProducer: ParticipantStatusUpdateKafkaProducer
) : IEntityUpdatesAspect {

    @GrpcClient("users-service")
    lateinit var childrenServiceStub: ChildrenServiceGrpc.ChildrenServiceBlockingStub

    private val coroutineScope = CoroutineScope(Dispatchers.Default + SupervisorJob())
    private val logger = LoggerFactory.getLogger(this.javaClass)

    @AfterReturning(pointcut = "execution(* org.careerseekers.cseventsservice.services.ChildToDirectionService.update(*))")
    override fun afterUpdate(joinPoint: JoinPoint) {
        val methodArgs = joinPoint.args[0] as UpdateChildToDirectionsDto
        if (methodArgs.status == null) return

        val childToDirectionRecord =
            childToDirectionService.getById(methodArgs.id, message = "Запись на компетенцию не найдена.") ?: return

        val fullChildDto =
            childrenServiceStub.getByIdFull(Id.newBuilder().setId(childToDirectionRecord.childId).build())

        coroutineScope.launch {
            try {
                kafkaProducer.sendMessage(
                    ParticipantStatusUpdate(
                        status = childToDirectionRecord.status,
                        user = fullChildDto.user.toCache(),
                        mentor = if (fullChildDto.hasMentor()) fullChildDto.mentor.toCache() else fullChildDto.user.toCache(),
                        childName = "${fullChildDto.lastName} ${fullChildDto.firstName} ${fullChildDto.patronymic}",
                        competitionName = childToDirectionRecord.direction.name,
                        ageCategory = childToDirectionRecord.directionAgeCategory.ageCategory.getAgeAlias()
                    )
                )
                logger.debug("Request for mail notification sent successfully. Ground: participant status update.")
            } catch (e: Exception) {
                logger.error("Failed to send mail notification for participates status update", e)
            }
        }
    }
}