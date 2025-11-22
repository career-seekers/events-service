package org.careerseekers.cseventsservice.controllers

import org.careerseekers.cseventsservice.utils.StatisticsStorage
import org.springframework.messaging.handler.annotation.MessageMapping
import org.springframework.messaging.handler.annotation.SendTo
import org.springframework.stereotype.Controller
import java.time.LocalDateTime
import java.util.concurrent.atomic.AtomicLong

@Controller
class WebSocketStatisticController {

    data class StatisticsMessage(
        val platformsCount: AtomicLong,
        val verifiedPlatformsCount: AtomicLong,
        val directionsCount: AtomicLong,
        val directionsWithoutDocs: AtomicLong,
        val directionDocsCount: AtomicLong,
        val lastDocumentUpload: LocalDateTime?,
    )

    @MessageMapping("/getStatistics")
    @SendTo("/events-service/topic/statistics")
    fun greetings(): StatisticsMessage {
        return StatisticsMessage(
            platformsCount = StatisticsStorage.platformsCount,
            verifiedPlatformsCount = StatisticsStorage.verifiedPlatformsCount,
            directionsCount = StatisticsStorage.directionsCount,
            directionsWithoutDocs = StatisticsStorage.directionsWithoutDocs,
            directionDocsCount = StatisticsStorage.directionDocsCount,
            lastDocumentUpload = StatisticsStorage.lastDocumentUpload
        )
    }
}