package org.careerseekers.cseventsservice.dto

import kotlinx.serialization.Polymorphic
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
@Polymorphic
sealed class KafkaMessagesDto : DtoClass

@Serializable
data class PlatformDto(
    val id: Long,
    val fullName: String,
    val shortName: String,
    val address: String,
    val userId: Long?,
)

@Serializable
@SerialName("PlatformCreation")
data class PlatformCreation (
    val platform: PlatformDto
) : KafkaMessagesDto()

@Serializable
@SerialName("DirectionCreation")
data class DirectionCreation(
    val name: String,
    val tutor: UsersCacheDto,
    val expert: UsersCacheDto,
) : KafkaMessagesDto()

@Serializable
@SerialName("DirectionDocumentsCreation")
data class DirectionDocumentsCreation (
    val documentType: String,
    val directionName: String,
    val ageCategory: String,
    val expert: UsersCacheDto,
    val tutor: UsersCacheDto,
) : KafkaMessagesDto()