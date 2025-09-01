package org.careerseekers.cseventsservice.serializers

import kotlinx.serialization.json.Json
import kotlinx.serialization.modules.SerializersModule
import kotlinx.serialization.modules.polymorphic
import org.careerseekers.cseventsservice.dto.CachesDto
import org.careerseekers.cseventsservice.dto.KafkaMessagesDto
import org.careerseekers.cseventsservice.dto.PlatformCreation
import org.careerseekers.cseventsservice.dto.UsersCacheDto

object CustomSerializerModule {
    val customSerializerModule = SerializersModule {
        polymorphic(CachesDto::class) {
            subclass(UsersCacheDto::class, UsersCacheDto.serializer())
        }
        polymorphic(KafkaMessagesDto::class) {
            subclass(PlatformCreation::class, PlatformCreation.serializer())
        }
    }

    val json = Json {
        serializersModule = customSerializerModule
        classDiscriminator = "type"
        ignoreUnknownKeys = true
    }
}