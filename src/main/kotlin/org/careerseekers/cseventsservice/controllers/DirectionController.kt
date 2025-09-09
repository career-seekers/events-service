package org.careerseekers.cseventsservice.controllers

import org.careerseekers.cseventsservice.controllers.interfaces.CrudController
import org.careerseekers.cseventsservice.dto.directions.CreateDirectionDto
import org.careerseekers.cseventsservice.dto.directions.UpdateDirectionDto
import org.careerseekers.cseventsservice.entities.Directions
import org.careerseekers.cseventsservice.enums.DirectionAgeCategory
import org.careerseekers.cseventsservice.io.converters.extensions.toHttpResponse
import org.careerseekers.cseventsservice.io.converters.extensions.toLongOrThrow
import org.careerseekers.cseventsservice.services.DirectionsService
import org.springframework.http.MediaType
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
@RequestMapping("/events-service/v1/directions")
class DirectionController(
    override val service: DirectionsService,
) : CrudController<Directions, Long, CreateDirectionDto, UpdateDirectionDto> {

    @GetMapping("/")
    override fun getAll() = service.getAll().toHttpResponse()

    @GetMapping("/{id}")
    override fun getById(@PathVariable id: Long) =
        service.getById(id, message = "Direction with id $id not found.")!!.toHttpResponse()

    @GetMapping("/getByUserId/{userId}")
    fun getByUserId(@PathVariable userId: Long) = service.getByUserId(userId).toHttpResponse()

    @GetMapping("/getByAgeCategory/{ageCategory}")
    fun getByAgeCategory(@PathVariable ageCategory: DirectionAgeCategory) =
        service.getByAgeCategory(ageCategory).toHttpResponse()

    @GetMapping("/getByAge/{age}")
    fun getByAge(@PathVariable age: Short) =
        service.getByAgeCategory(DirectionAgeCategory.getAgeCategory(age)).toHttpResponse()

    @PostMapping("/")
    override fun create(@RequestBody item: CreateDirectionDto) = service.create(item).toHttpResponse()

    @PostMapping("/createAll")
    override fun createAll(@RequestBody items: List<CreateDirectionDto>) = service.createAll(items).toHttpResponse()

    @PatchMapping("/")
    override fun update(@RequestBody item: UpdateDirectionDto) = service.update(item).toHttpResponse()

    @PatchMapping("/uploadDirectionIcon", consumes = [MediaType.MULTIPART_FORM_DATA_VALUE])
    fun uploadDirectionIcon(
        @RequestPart("id") id: String,
        @RequestPart("file") file: MultipartFile,
    ) = service.update(
        UpdateDirectionDto(
            id = id.toLongOrThrow(),
            icon = file
        )
    ).toHttpResponse()

    @DeleteMapping("/{id}")
    override fun deleteById(@PathVariable id: Long) = service.deleteById(id).toHttpResponse()

    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/")
    override fun deleteAll() = service.deleteAll().toHttpResponse()
}