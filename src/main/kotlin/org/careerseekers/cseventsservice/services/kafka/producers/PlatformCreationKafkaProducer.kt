package org.careerseekers.cseventsservice.services.kafka.producers

import org.careerseekers.cseventsservice.dto.PlatformCreation
import org.careerseekers.cseventsservice.enums.KafkaTopics
import org.springframework.kafka.core.KafkaTemplate
import org.springframework.stereotype.Service

@Service
class PlatformCreationKafkaProducer(
    override val template: KafkaTemplate<String, PlatformCreation>
) : CustomKafkaProducer<PlatformCreation> {
    override val topic = KafkaTopics.PLATFORM_CREATION
}