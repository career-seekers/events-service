package org.careerseekers.cseventsservice.services.kafka.producers

import org.careerseekers.cseventsservice.dto.EventCreationDto
import org.careerseekers.cseventsservice.enums.KafkaTopics
import org.springframework.kafka.core.KafkaTemplate
import org.springframework.stereotype.Service

@Service
class EventCreationProducer(
    override val template: KafkaTemplate<String, EventCreationDto>
) : CustomKafkaProducer<EventCreationDto> {
    override val topic = KafkaTopics.EVENT_CREATION
}