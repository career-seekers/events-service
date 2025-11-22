package org.careerseekers.cseventsservice.aspects

import org.aspectj.lang.JoinPoint
import org.aspectj.lang.annotation.AfterReturning
import org.aspectj.lang.annotation.Aspect
import org.careerseekers.cseventsservice.aspects.interfaces.IEntityUpdatesAspect
import org.careerseekers.cseventsservice.controllers.WebSocketStatisticController
import org.careerseekers.cseventsservice.utils.StatisticsScrapperService
import org.careerseekers.cseventsservice.utils.StatisticsStorage
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Component

@Aspect
@Component
class DirectionsUpdatesAspect(
    private val statisticsScrapperService: StatisticsScrapperService,
    private val webSocketStatisticController: WebSocketStatisticController,
) : IEntityUpdatesAspect {

    private val logger = LoggerFactory.getLogger(this.javaClass)

    @AfterReturning("@annotation(org.careerseekers.cseventsservice.annotations.DirectionsUpdate)")
    override fun afterUpdate(joinPoint: JoinPoint) {
        statisticsScrapperService.setDirectionsCount()
        statisticsScrapperService.setDirectionsWithoutDocs()

        webSocketStatisticController.sendStatisticsManually()
        logger.info("Directions count updated, the caller method is ${joinPoint.signature.name}. Total directions: ${StatisticsStorage.directionsCount}, direction without docs: ${StatisticsStorage.directionsWithoutDocs}")
    }
}