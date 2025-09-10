package org.careerseekers.cseventsservice.services.kafka.producers

import org.careerseekers.cseventsservice.dto.DirectionCreation
import org.careerseekers.cseventsservice.enums.KafkaTopics
import org.springframework.kafka.core.KafkaTemplate
import org.springframework.stereotype.Service

@Service
class DirectionCreationKafkaProducer(
    override val template: KafkaTemplate<String, DirectionCreation>
) : CustomKafkaProducer<DirectionCreation> {
    override val topic = KafkaTopics.DIRECTION_CREATION
}