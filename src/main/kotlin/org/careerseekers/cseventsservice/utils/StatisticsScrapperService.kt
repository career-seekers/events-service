package org.careerseekers.cseventsservice.utils

import org.careerseekers.cseventsservice.enums.VerificationStatus
import org.careerseekers.cseventsservice.repositories.DirectionDocumentsRepository
import org.careerseekers.cseventsservice.repositories.DirectionsRepository
import org.careerseekers.cseventsservice.repositories.EventsRepository
import org.careerseekers.cseventsservice.repositories.PlatformsRepository
import org.springframework.beans.factory.SmartInitializingSingleton

@Utility
class StatisticsScrapperService(
    private val platformsRepository: PlatformsRepository,
    private val directionsRepository: DirectionsRepository,
    private val directionDocumentsRepository: DirectionDocumentsRepository,
    private val eventsRepository: EventsRepository,
) : SmartInitializingSingleton {

    override fun afterSingletonsInstantiated() {
        setPlatformsCount()
        setDirectionsCount()
        setDirectionsWithoutDocs()
        setDirectionDocsCount()
        setLastDocumentUpload()
        setEventsCount()
        setVerifiedEventsCount()
    }


    fun setPlatformsCount() {
        StatisticsStorage.setPlatformsCount(platformsRepository.count())
        StatisticsStorage.setVerifiedPlatformsCount(platformsRepository.countByVerified(true))
    }

    fun setDirectionsCount() = StatisticsStorage.setDirectionsCount(directionsRepository.count())

    fun setDirectionsWithoutDocs() =
        StatisticsStorage.setDirectionsWithoutDocs(directionsRepository.countByDocumentsIsNull())

    fun setDirectionDocsCount() = StatisticsStorage.setDirectionDocsCount(directionsRepository.count())

    fun setLastDocumentUpload() = StatisticsStorage.setLastDocumentUpload(
        directionDocumentsRepository
            .findAll()
            .sortedByDescending { it.createdAt }
            .firstOrNull()
            ?.createdAt
    )

    fun setEventsCount() = StatisticsStorage.setEventsCount(eventsRepository.count())

    fun setVerifiedEventsCount() = StatisticsStorage.setVerifiedEventsCount(
        eventsRepository.countEventsByVerified(
            VerificationStatus.ACCEPTED
        )
    )
}