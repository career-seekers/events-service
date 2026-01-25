package org.careerseekers.cseventsservice.dto

import kotlinx.serialization.Polymorphic
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import org.careerseekers.cseventsservice.enums.DirectionDocsEventTypes
import org.careerseekers.cseventsservice.enums.EventTypes
import org.careerseekers.cseventsservice.enums.ParticipantStatus
import org.careerseekers.cseventsservice.enums.StatisticsUpdateRequestTypes

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
data class DirectionDocumentsTask (
    val eventType: DirectionDocsEventTypes,
    val documentType: String,
    val directionName: String,
    val ageCategory: String,
    val expert: UsersCacheDto,
    val tutor: UsersCacheDto,
    val verification: Boolean,
) : KafkaMessagesDto()

@Serializable
@SerialName("ParticipantStatusUpdate")
data class ParticipantStatusUpdate (
    val status: ParticipantStatus,
    val user: UsersCacheDto,
    val mentor: UsersCacheDto,
    val childName: String,
    val competitionName: String,
    val ageCategory: String,
) : KafkaMessagesDto()

@Serializable
@SerialName("EventCreationDto")
data class EventCreationDto(
    val eventType: EventTypes,
    val directionName: String,
    val ageCategory: String,
    val expertName: String,
    val expertEmail: String,
    val participantsEmailList: List<String>
) : KafkaMessagesDto()

@Serializable
@SerialName("StatisticsUpdateDto")
data class StatisticsUpdateRequestDto(
    val type: StatisticsUpdateRequestTypes
) : KafkaMessagesDto()