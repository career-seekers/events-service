package org.careerseekers.cseventsservice.services.notification

import org.careerseekers.cseventsservice.cache.UsersCacheClient
import org.careerseekers.cseventsservice.dto.DirectionDocumentsTask
import org.careerseekers.cseventsservice.entities.DirectionDocuments
import org.careerseekers.cseventsservice.enums.DirectionAgeCategory.Companion.getAgeAlias
import org.careerseekers.cseventsservice.enums.DirectionDocsEventTypes
import org.careerseekers.cseventsservice.enums.FileTypes.Companion.getAlias
import org.careerseekers.cseventsservice.exceptions.BadRequestException
import org.careerseekers.cseventsservice.services.DirectionsService
import org.careerseekers.cseventsservice.services.interfaces.INotificationService
import org.careerseekers.cseventsservice.services.kafka.producers.CustomKafkaProducer
import org.springframework.stereotype.Service

@Service
class DirectionDocsNotificationService(
    override val kafkaProducer: CustomKafkaProducer<DirectionDocumentsTask>,
    private val directionsService: DirectionsService,
    private val usersCacheClient: UsersCacheClient,
) :
    INotificationService<DirectionDocumentsTask> {

    fun sendNotification(docs: DirectionDocuments, event: DirectionDocsEventTypes) {
        val direction = directionsService.getById(
            docs.direction!!.id,
            message = "Профессия с ID ${docs.direction!!.id} не найдена."
        )!!
        val expert = usersCacheClient.getItemFromCache(direction.expertId!!)
            ?: throw BadRequestException("Пользователь с ID ${direction.expertId} не найден.")
        val tutor = usersCacheClient.getItemFromCache(direction.userId!!)
            ?: throw BadRequestException("Пользователь с ID ${direction.userId} не найден.")

        notify(
            DirectionDocumentsTask(
                eventType = event,
                documentType = docs.documentType.getAlias(),
                directionName = direction.name,
                ageCategory = docs.ageCategory?.getAgeAlias()
                    ?: throw BadRequestException("У документа должна быть указана возрастная категория."),
                expert = expert,
                tutor = tutor,
                verification = docs.verified ?: false,
            )
        )
    }
}