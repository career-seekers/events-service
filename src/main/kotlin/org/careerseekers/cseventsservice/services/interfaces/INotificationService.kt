package org.careerseekers.cseventsservice.services.interfaces

import org.careerseekers.cseventsservice.dto.KafkaMessagesDto
import org.careerseekers.cseventsservice.services.kafka.producers.CustomKafkaProducer

interface INotificationService<T : KafkaMessagesDto> {
    val kafkaProducer: CustomKafkaProducer<T>

    fun notify(message: T) = kafkaProducer.sendMessage(message)
}