package org.careerseekers.cseventsservice.config

import org.apache.kafka.clients.admin.NewTopic
import org.careerseekers.cseventsservice.enums.KafkaTopics
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.kafka.config.TopicBuilder

@Configuration
class KafkaTopicsConfig {

    @Bean
    fun platformCreationTopic(): NewTopic {
        return TopicBuilder
            .name(KafkaTopics.PLATFORM_CREATION.name)
            .partitions(12)
            .replicas(3)
            .build()
    }

    @Bean
    fun directionCreationTopic(): NewTopic {
        return TopicBuilder
            .name(KafkaTopics.DIRECTION_CREATION.name)
            .partitions(12)
            .replicas(3)
            .build()
    }
}