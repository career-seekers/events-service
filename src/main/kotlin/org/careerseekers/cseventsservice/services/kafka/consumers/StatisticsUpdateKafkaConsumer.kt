package org.careerseekers.cseventsservice.services.kafka.consumers

import org.apache.kafka.clients.consumer.ConsumerRecord
import org.careerseekers.cseventsservice.controllers.WebSocketStatisticController
import org.careerseekers.cseventsservice.dto.StatisticsUpdateRequestDto
import org.careerseekers.cseventsservice.enums.StatisticsUpdateRequestTypes
import org.careerseekers.cseventsservice.utils.StatisticsScrapperService
import org.springframework.kafka.annotation.KafkaListener
import org.springframework.kafka.support.Acknowledgment
import org.springframework.stereotype.Service

@Service
class StatisticsUpdateKafkaConsumer(
    private val statisticsScrapperService: StatisticsScrapperService,
    private val webSocketStatisticController: WebSocketStatisticController
) :
    CustomKafkaConsumer<String, StatisticsUpdateRequestDto> {

    @KafkaListener(
        topics = ["STATISTICS_UPDATE"],
        groupId = "STATISTICS_UPDATER"
    )
    override fun receiveMessage(
        consumerRecord: ConsumerRecord<String, StatisticsUpdateRequestDto>,
        acknowledgment: Acknowledgment
    ) {
        when (consumerRecord.value().updateRequestType) {
            StatisticsUpdateRequestTypes.PLATFORMS_UPDATE -> {
                statisticsScrapperService.setPlatformsCount()
            }

            StatisticsUpdateRequestTypes.DIRECTIONS_UPDATE -> {
                statisticsScrapperService.setDirectionsCount()
                statisticsScrapperService.setDirectionsWithoutDocs()
            }

            StatisticsUpdateRequestTypes.DIRECTION_DOCUMENTS_UPDATE -> {
                statisticsScrapperService.setDirectionsWithoutDocs()
                statisticsScrapperService.setDirectionDocsCount()
                statisticsScrapperService.setLastDocumentUpload()
            }

            StatisticsUpdateRequestTypes.EVENTS_UPDATE -> {
                statisticsScrapperService.setEventsCount()
                statisticsScrapperService.setVerifiedEventsCount()
            }
        }

        webSocketStatisticController.sendStatisticsManually()
        acknowledgment.acknowledge()
    }
}