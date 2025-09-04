package org.careerseekers.cseventsservice.controllers

import org.careerseekers.cseventsservice.controllers.interfaces.CrudController
import org.careerseekers.cseventsservice.dto.platforms.ChangePlatformOwnerDto
import org.careerseekers.cseventsservice.dto.platforms.CreatePlatformDto
import org.careerseekers.cseventsservice.dto.platforms.UpdatePlatformDto
import org.careerseekers.cseventsservice.entities.Platforms
import org.careerseekers.cseventsservice.io.BasicSuccessfulResponse
import org.careerseekers.cseventsservice.io.converters.extensions.toHttpResponse
import org.careerseekers.cseventsservice.services.PlatformsService
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PatchMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/events-service/v1/platforms")
class PlatformsController(
    override val service: PlatformsService,
) : CrudController<Platforms, Long, CreatePlatformDto, UpdatePlatformDto> {

    @GetMapping("/")
    override fun getAll(): BasicSuccessfulResponse<List<Platforms>> = service.getAll().toHttpResponse()

    @GetMapping("/{id}")
    override fun getById(@PathVariable id: Long): BasicSuccessfulResponse<Platforms> {
        return service.getById(
            id,
            throwable = true,
            message = "Platform with id $id not found."
        )!!.toHttpResponse()
    }

    @PostMapping("/")
    override fun create(@RequestBody item: CreatePlatformDto): BasicSuccessfulResponse<Platforms> =
        service.create(item).toHttpResponse()

    @PostMapping("/createAll")
    override fun createAll(@RequestBody items: List<CreatePlatformDto>): BasicSuccessfulResponse<String> =
        service.createAll(items).toHttpResponse()

    @PatchMapping("/")
    override fun update(@RequestBody item: UpdatePlatformDto): BasicSuccessfulResponse<String> =
        service.update(item).toHttpResponse()

    @PreAuthorize("hasRole('ADMIN')")
    @PatchMapping("/updatePlatformOwner")
    fun updatePlatformOwner(@RequestBody item: ChangePlatformOwnerDto): BasicSuccessfulResponse<String> =
        service.updatePlatformOwner(item).toHttpResponse()

    @PreAuthorize("hasRole('ADMIN')")
    @PatchMapping("/verify/{id}")
    fun updatePlatformVerification(@PathVariable id: Long): BasicSuccessfulResponse<String> =
        service.updatePlatformVerification(id).toHttpResponse()

    @DeleteMapping("/{id}")
    override fun deleteById(@PathVariable id: Long): BasicSuccessfulResponse<String> =
        service.deleteById(id).toHttpResponse()

    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/")
    override fun deleteAll(): BasicSuccessfulResponse<String> = service.deleteAll().toHttpResponse()
}