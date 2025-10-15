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
class PlatformUpdatesAspect(private val statisticsScrapperService: StatisticsScrapperService) : IEntityUpdatesAspect {

    private val logger = LoggerFactory.getLogger(this.javaClass)

    @AfterReturning("@annotation(org.careerseekers.cseventsservice.annotations.PlatformsUpdate)")
    override fun afterUpdate(joinPoint: JoinPoint) {
        statisticsScrapperService.setPlatformsCount()

        logger.info("Platforms count updated, the caller method is ${joinPoint.signature.name}. Total platforms: ${StatisticsStorage.platformsCount}.")
    }
}