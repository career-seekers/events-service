package org.careerseekers.cseventsservice.services

import org.careerseekers.cseventsservice.dto.docs.CreateDirectionDocumentDto
import org.careerseekers.cseventsservice.dto.docs.UpdateDirectionDocumentDto
import org.careerseekers.cseventsservice.entities.DirectionDocuments
import org.careerseekers.cseventsservice.enums.DirectionDocsEventTypes
import org.careerseekers.cseventsservice.exceptions.NotFoundException
import org.careerseekers.cseventsservice.mappers.DirectionDocumentsMapper
import org.careerseekers.cseventsservice.repositories.DirectionDocumentsRepository
import org.careerseekers.cseventsservice.services.interfaces.crud.ICreateService
import org.careerseekers.cseventsservice.services.interfaces.crud.IDeleteService
import org.careerseekers.cseventsservice.services.interfaces.crud.IReadService
import org.careerseekers.cseventsservice.services.notification.DirectionDocsNotificationService
import org.careerseekers.cseventsservice.utils.DocumentsApiResolver
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.time.LocalDateTime

@Service
class DirectionDocumentsService(
    override val repository: DirectionDocumentsRepository,
    private val directionDocumentsMapper: DirectionDocumentsMapper,
    private val documentsApiResolver: DocumentsApiResolver,
    private val directionsService: DirectionsService,
    private val directionDocsNotificationService: DirectionDocsNotificationService,
) : IReadService<DirectionDocuments, Long>,
    ICreateService<DirectionDocuments, Long, CreateDirectionDocumentDto>,
    IDeleteService<DirectionDocuments, Long> {

    fun getByUserId(userId: Long) = repository.findByUserId(userId)

    fun getByDirectionId(id: Long) = repository.findByDirectionId(id)

    @Transactional
    override fun create(item: CreateDirectionDocumentDto): DirectionDocuments {
        val direction =
            directionsService.getById(item.directionId, message = "Компетенция с ID ${item.directionId} не найдена.")!!

        return repository.save(
            directionDocumentsMapper.directionDocsFromDto(
                item.copy(
                    documentId = documentsApiResolver.loadDocId(
                        url = "uploadDirectionsDocument",
                        file = item.document,
                        docType = item.documentType,
                        isDirection = true
                    ),
                    createdAt = LocalDateTime.now(),
                    direction = direction
                )
            )
        ).also { directionDocsNotificationService.sendNotification(it, DirectionDocsEventTypes.CREATION) }
    }

    @Transactional
    fun update(item: UpdateDirectionDocumentDto): String {
        getById(item.id, message = "Документ с ID '${item.id}' не найден.")!!.apply {
            item.documentType?.let { documentType = it }
            item.ageCategory?.let { ageCategory = it }
        }.also(repository::save)

        return "Документ компетенции обновлен успешно."
    }

    @Transactional
    fun verifyDirectionDocs(id: Long): String {
        return getById(id, throwable = false)?.let { doc ->
            doc.verified = !doc.verified
            repository.save(doc).also { directionDocsNotificationService.sendNotification(it, DirectionDocsEventTypes.VERIFICATION) }

            if (doc.verified) {
                "Документ верифицирован."
            } else {
                "Верификация документа отменена."
            }
        } ?: throw NotFoundException("Документ компетенции с ID $id не найден.")
    }

    @Transactional
    override fun deleteById(id: Long): String {
        getById(id, message = "Документ с ID '${id}' не найден.")!!.apply(repository::delete)

        return "Документ удален успешно."
    }

    @Transactional
    override fun deleteAll(): String {
        repository.deleteAll()

        return "Все документы компетенций удалены успешно."
    }
}