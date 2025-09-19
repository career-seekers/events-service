package org.careerseekers.cseventsservice.services.kafka.producers

import org.careerseekers.cseventsservice.dto.DirectionDocumentsTask
import org.careerseekers.cseventsservice.enums.KafkaTopics
import org.springframework.kafka.core.KafkaTemplate
import org.springframework.stereotype.Service

@Service
class DirectionDocumentsTasksKafkaProducer(
    override val template: KafkaTemplate<String, DirectionDocumentsTask>
) : CustomKafkaProducer<DirectionDocumentsTask> {
    override val topic = KafkaTopics.DIRECTION_DOCUMENTS_TASKS
}