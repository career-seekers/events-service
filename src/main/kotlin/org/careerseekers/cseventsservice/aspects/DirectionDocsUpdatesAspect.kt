package org.careerseekers.cseventsservice.aspects

import org.aspectj.lang.JoinPoint
import org.aspectj.lang.annotation.AfterReturning
import org.aspectj.lang.annotation.Aspect
import org.careerseekers.cseventsservice.aspects.interfaces.IEntityUpdatesAspect
import org.careerseekers.cseventsservice.utils.StatisticsScrapperService
import org.careerseekers.cseventsservice.utils.StatisticsStorage
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Component

@Aspect
@Component
class DirectionDocsUpdatesAspect(private val statisticsScrapperService: StatisticsScrapperService) :
    IEntityUpdatesAspect {

    private val logger = LoggerFactory.getLogger(this.javaClass)

    @AfterReturning("@annotation(org.careerseekers.cseventsservice.annotations.DirectionDocsUpdate)")
    override fun afterUpdate(joinPoint: JoinPoint) {
        statisticsScrapperService.setDirectionDocsCount()
        statisticsScrapperService.setLastDocumentUpload()

        logger.info("Directions documents count updated, the caller method is ${joinPoint.signature.name}. Total documents: ${StatisticsStorage.directionDocsCount}, last document upload: ${StatisticsStorage.lastDocumentUpload}.")
    }
}