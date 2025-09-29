package org.careerseekers.cseventsservice.controllers

import org.careerseekers.cseventsservice.dto.directions.childToDirection.LinkChildWithDirectionDto
import org.careerseekers.cseventsservice.io.converters.extensions.toHttpResponse
import org.careerseekers.cseventsservice.services.ChildToDirectionService
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/events-service/v1/childToDirection")
class ChildToDirectionController(private val service: ChildToDirectionService) {

    @GetMapping("/")
    fun getAll() = service.getAll().toHttpResponse()

    @GetMapping("/{id}")
    fun getById(@PathVariable id: Long) =
        service.getById(id, message = "Запись об участии в компетенции с ID $id не найдена.")!!.toHttpResponse()

    @GetMapping("/getByChildId/{id}")
    fun getByChildId(@PathVariable id: Long) = service.getByChildId(id).toHttpResponse()

    @GetMapping("/getByDirectionId/{id}")
    fun getByDirectionId(@PathVariable id: Long) = service.getByDirectionId(id).toHttpResponse()

    @PostMapping("/")
    fun create(@RequestBody item: LinkChildWithDirectionDto) = service.create(item).toHttpResponse()
}