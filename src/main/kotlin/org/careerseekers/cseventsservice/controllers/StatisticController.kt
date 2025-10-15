package org.careerseekers.cseventsservice.controllers

import org.careerseekers.cseventsservice.utils.StatisticsStorage
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/events-service/v1/statistics")
class StatisticController {

    @GetMapping("/getPlatformsCount")
    fun getPlatformsCount() = StatisticsStorage.platformsCount

    @GetMapping("/getVerifiedPlatformsCount")
    fun getVerifiedPlatformsCount() = StatisticsStorage.verifiedPlatformsCount

    @GetMapping("/getDirectionsCount")
    fun getDirectionsCount() = StatisticsStorage.directionsCount

    @GetMapping("/getDirectionsWithoutDocsCount")
    fun getDirectionsWithoutDocsCount() = StatisticsStorage.directionsWithoutDocs

    @GetMapping("/getDirectionsDocsCount")
    fun getDirectionDocsCount() = StatisticsStorage.directionDocsCount

    @GetMapping("/getLastDocumentUpload")
    fun getLastDocumentUpload() = StatisticsStorage.lastDocumentUpload
}