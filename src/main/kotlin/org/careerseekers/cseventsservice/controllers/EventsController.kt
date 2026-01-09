package org.careerseekers.cseventsservice.controllers

import org.careerseekers.cseventsservice.controllers.interfaces.PagedCrudController
import org.careerseekers.cseventsservice.dto.events.CreateEventDto
import org.careerseekers.cseventsservice.dto.events.EventsFilterDto
import org.careerseekers.cseventsservice.dto.events.UpdateEventDto
import org.careerseekers.cseventsservice.dto.events.UpdateEventVerificationDto
import org.careerseekers.cseventsservice.entities.Events
import org.careerseekers.cseventsservice.io.converters.extensions.toHttpResponse
import org.careerseekers.cseventsservice.services.EventsService
import org.springframework.data.domain.Pageable
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.ModelAttribute
import org.springframework.web.bind.annotation.PatchMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/events-service/v1/events")
class EventsController(
    override val service: EventsService
) : PagedCrudController<Events, Long, CreateEventDto, UpdateEventDto, EventsFilterDto> {

    @GetMapping("/")
    override fun getAll(
        @ModelAttribute filters: EventsFilterDto,
        pageable: Pageable
    ) = service.getAll(filters, pageable).toHttpResponse()

    @GetMapping("/{id}")
    override fun getById(@PathVariable id: Long) = service.getById(id, message = "Событие с ID $id не найдено.")!!.toHttpResponse()

    @GetMapping("/getByDirectionId/{id}")
    fun getByDirectionId(@PathVariable id: Long) = service.getByDirectionId(id).toHttpResponse()

    @GetMapping("/getByAgeCategoryId/{id}")
    fun getByAgeCategoryId(@PathVariable id: Long) = service.getByAgeCategoryId(id).toHttpResponse()

    @PostMapping("/")
    override fun create(@RequestBody item: CreateEventDto) = service.create(item).toHttpResponse()

    @PostMapping("/createAll")
    override fun createAll(items: List<CreateEventDto>) = service.createAll(items).toHttpResponse()

    @PatchMapping("/")
    override fun update(@RequestBody item: UpdateEventDto) = service.update(item).toHttpResponse()

    @PreAuthorize("hasAuthority('ADMIN')")
    @PatchMapping("/verify")
    fun verifyEvent(@RequestBody item: UpdateEventVerificationDto) = service.verifyEvent(item).toHttpResponse()

    @DeleteMapping("/{id}")
    override fun deleteById(@PathVariable id: Long) = service.deleteById(id).toHttpResponse()

    @PreAuthorize("hasAuthority('ADMIN')")
    @DeleteMapping("/")
    override fun deleteAll() = service.deleteAll().toHttpResponse()
}