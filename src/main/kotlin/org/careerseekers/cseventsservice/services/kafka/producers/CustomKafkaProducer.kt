package org.careerseekers.cseventsservice.services.kafka.producers

import org.careerseekers.cseventsservice.dto.KafkaMessagesDto
import org.careerseekers.cseventsservice.enums.KafkaTopics
import org.springframework.kafka.core.KafkaTemplate

interface CustomKafkaProducer<T : KafkaMessagesDto> {
    val topic: KafkaTopics
    val template: KafkaTemplate<String, T>

    fun sendMessage(message: T) {
        template.send(topic.name, message)
    }
}