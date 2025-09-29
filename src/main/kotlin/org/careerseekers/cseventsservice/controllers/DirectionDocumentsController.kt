package org.careerseekers.cseventsservice.controllers

import org.careerseekers.cseventsservice.controllers.interfaces.crud.IDeleteController
import org.careerseekers.cseventsservice.controllers.interfaces.crud.IReadController
import org.careerseekers.cseventsservice.dto.docs.CreateDirectionDocumentDto
import org.careerseekers.cseventsservice.dto.docs.UpdateDirectionDocumentDto
import org.careerseekers.cseventsservice.entities.DirectionDocuments
import org.careerseekers.cseventsservice.enums.DirectionAgeCategory
import org.careerseekers.cseventsservice.enums.FileTypes
import org.careerseekers.cseventsservice.io.BasicSuccessfulResponse
import org.careerseekers.cseventsservice.io.converters.extensions.toHttpResponse
import org.careerseekers.cseventsservice.io.converters.extensions.toLongOrThrow
import org.careerseekers.cseventsservice.services.DirectionDocumentsService
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PatchMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestPart
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.multipart.MultipartFile

@RestController
@RequestMapping("/events-service/v1/direction-docs/")
@PreAuthorize("hasAnyAuthority('ADMIN', 'TUTOR', 'EXPERT')")
class DirectionDocumentsController(override val service: DirectionDocumentsService) :
    IReadController<DirectionDocuments, Long>,
    IDeleteController<DirectionDocuments, Long> {

    @GetMapping("/")
    override fun getAll() = service.getAll().toHttpResponse()

    @GetMapping("/{id}")
    override fun getById(@PathVariable id: Long) =
        service.getById(id, message = "Документ компетенции с ID $id не найден.")!!.toHttpResponse()

    @GetMapping("/getByUserId/{userId}")
    fun getByUserId(@PathVariable userId: Long) = service.getByUserId(userId).toHttpResponse()

    @GetMapping("/getByDirectoryId/{id}")
    fun getByDirectoryId(@PathVariable id: Long) = service.getByDirectionId(id).toHttpResponse()

    @PostMapping("/")
    fun create(
        @RequestPart("documentType") documentType: String,
        @RequestPart("ageCategory") ageCategory: String,
        @RequestPart("document") document: MultipartFile,
        @RequestPart("userId") userId: String,
        @RequestPart("directionId") directionId: String,
    ): BasicSuccessfulResponse<DirectionDocuments> {
        return service.create(CreateDirectionDocumentDto(
            documentType = FileTypes.valueOf(documentType.uppercase().trim().trim('"')),
            ageCategory = DirectionAgeCategory.valueOf(ageCategory.uppercase().trim().trim('"')),
            document = document,
            userId = userId.toLongOrThrow(),
            directionId = directionId.toLongOrThrow(),
        )).toHttpResponse()
    }

    @PatchMapping("/")
    fun updateDocumentType(@RequestBody item: UpdateDirectionDocumentDto) =
        service.update(item).toHttpResponse()

    @PreAuthorize("hasAuthority('ADMIN')")
    @PatchMapping("/verify/{id}/{status}")
    fun verify(@PathVariable id: Long, @PathVariable status: Boolean) = service.verifyDirectionDocs(id, status).toHttpResponse()

    @DeleteMapping("/{id}")
    override fun deleteById(@PathVariable id: Long) = service.deleteById(id).toHttpResponse()

    @PreAuthorize("hasAuthority('ADMIN')")
    @DeleteMapping("/")
    override fun deleteAll() = service.deleteAll().toHttpResponse()
}