package org.careerseekers.cseventsservice.services.kafka.producers

import org.careerseekers.cseventsservice.dto.DirectionDocumentsCreation
import org.careerseekers.cseventsservice.enums.KafkaTopics
import org.springframework.kafka.core.KafkaTemplate
import org.springframework.stereotype.Service

@Service
class DirectionDocumentsCreationKafkaProducer(
    override val template: KafkaTemplate<String, DirectionDocumentsCreation>
) : CustomKafkaProducer<DirectionDocumentsCreation> {
    override val topic = KafkaTopics.DIRECTION_DOCUMENTS_CREATION
}