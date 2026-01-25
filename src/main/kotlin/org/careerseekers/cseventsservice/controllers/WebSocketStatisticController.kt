package org.careerseekers.cseventsservice.controllers

import org.careerseekers.cseventsservice.utils.StatisticsStorage
import org.springframework.messaging.handler.annotation.MessageMapping
import org.springframework.messaging.handler.annotation.SendTo
import org.springframework.messaging.simp.SimpMessagingTemplate
import org.springframework.stereotype.Controller
import java.time.LocalDateTime
import java.util.concurrent.atomic.AtomicLong

@Controller
class WebSocketStatisticController(private val messagingTemplate: SimpMessagingTemplate) {

    data class StatisticsMessage(
        val platformsCount: AtomicLong,
        val verifiedPlatformsCount: AtomicLong,
        val directionsCount: AtomicLong,
        val directionsWithoutDocs: AtomicLong,
        val directionDocsCount: AtomicLong,
        val lastDocumentUpload: LocalDateTime?,
        val eventsCount: AtomicLong,
        val verifiedEventsCount: AtomicLong,
    )

    @MessageMapping("/getStatistics")
    @SendTo("/events-service/topic/statistics")
    fun sendStatistics(): StatisticsMessage {
        return StatisticsMessage(
            platformsCount = StatisticsStorage.platformsCount,
            verifiedPlatformsCount = StatisticsStorage.verifiedPlatformsCount,
            directionsCount = StatisticsStorage.directionsCount,
            directionsWithoutDocs = StatisticsStorage.directionsWithoutDocs,
            directionDocsCount = StatisticsStorage.directionDocsCount,
            lastDocumentUpload = StatisticsStorage.lastDocumentUpload,
            eventsCount = StatisticsStorage.eventsCount,
            verifiedEventsCount = StatisticsStorage.verifiedEventsCount,
        )
    }

    fun sendStatisticsManually() {
        val statistics = sendStatistics()
        messagingTemplate.convertAndSend("/events-service/topic/statistics", statistics)
    }
}