package org.careerseekers.cseventsservice.services.kafka.producers

import org.careerseekers.cseventsservice.dto.ParticipantStatusUpdate
import org.careerseekers.cseventsservice.enums.KafkaTopics
import org.springframework.kafka.core.KafkaTemplate
import org.springframework.stereotype.Service

@Service
class ParticipantStatusUpdateKafkaProducer(
    override val template: KafkaTemplate<String, ParticipantStatusUpdate>
) : CustomKafkaProducer<ParticipantStatusUpdate> {
    override val topic = KafkaTopics.PARTICIPATION_STATUS_UPDATE
}