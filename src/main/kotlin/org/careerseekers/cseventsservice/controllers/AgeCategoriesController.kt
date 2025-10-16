package org.careerseekers.cseventsservice.controllers

import org.careerseekers.cseventsservice.dto.directions.categories.UpdateCategoryOpennessDto
import org.careerseekers.cseventsservice.io.converters.extensions.toHttpResponse
import org.careerseekers.cseventsservice.services.DirectionAgeCategoriesService
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PatchMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("events-service/v1/ageCategories")
class AgeCategoriesController(private val service: DirectionAgeCategoriesService) {

    @GetMapping("/getByDirectionId/{id}")
    fun getByDirectionId(@PathVariable("id") id: Long) = service.getByDirectionId(id).toHttpResponse()

    @PatchMapping("/updatePublicity")
    fun updatePublicity(@RequestBody item: UpdateCategoryOpennessDto) = service.updatePublicity(item).toHttpResponse()
}