package org.careerseekers.cseventsservice.utils

import org.careerseekers.cseventsservice.services.DirectionDocumentsService
import org.careerseekers.cseventsservice.services.DirectionsService
import org.careerseekers.cseventsservice.services.PlatformsService
import org.springframework.beans.factory.SmartInitializingSingleton

@Utility
class StatisticsScrapperService(
    private val platformsService: PlatformsService,
    private val directionsService: DirectionsService,
    private val directionDocumentsService: DirectionDocumentsService
) : SmartInitializingSingleton {

    override fun afterSingletonsInstantiated() {
        setPlatformsCount()
        setDirectionsCount()
        setDirectionsWithoutDocs()
        setDirectionDocsCount()
        setLastDocumentUpload()
    }


    fun setPlatformsCount() {
        val platforms = platformsService.getAll()

        StatisticsStorage.setPlatformsCount(platforms.size.toLong())
        StatisticsStorage.setVerifiedPlatformsCount(platforms.filter { it.verified }.size.toLong())
    }

    fun setDirectionsCount() = StatisticsStorage.setDirectionsCount(directionsService.getAll().size.toLong())

    fun setDirectionsWithoutDocs() = StatisticsStorage.setDirectionsWithoutDocs(
        directionsService.getAll().filter { it.documents == null }.size.toLong()
    )

    fun setDirectionDocsCount() =
        StatisticsStorage.setDirectionDocsCount(directionDocumentsService.getAll().size.toLong())

    fun setLastDocumentUpload() = StatisticsStorage.setLastDocumentUpload(
        directionDocumentsService
            .getAll()
            .sortedByDescending { it.createdAt }
            .firstOrNull()
            ?.createdAt
    )
}