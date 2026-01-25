package org.careerseekers.cseventsservice.services.kafka.producers

import org.careerseekers.cseventsservice.dto.StatisticsUpdateRequestDto
import org.careerseekers.cseventsservice.enums.KafkaTopics
import org.springframework.kafka.core.KafkaTemplate
import org.springframework.stereotype.Service

@Service
class StatisticsUpdateKafkaProducer(override val template: KafkaTemplate<String, StatisticsUpdateRequestDto>) :
    CustomKafkaProducer<StatisticsUpdateRequestDto> {
    override val topic = KafkaTopics.STATISTICS_UPDATE
}