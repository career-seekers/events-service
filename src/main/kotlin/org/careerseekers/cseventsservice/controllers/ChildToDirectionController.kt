package org.careerseekers.cseventsservice.controllers

import org.careerseekers.cseventsservice.annotations.AccessUntil
import org.careerseekers.cseventsservice.dto.directions.childToDirection.CreateChildWithDirectionDto
import org.careerseekers.cseventsservice.dto.directions.childToDirection.SetTeacherInfoDto
import org.careerseekers.cseventsservice.dto.directions.childToDirection.UpdateChildToDirectionsDto
import org.careerseekers.cseventsservice.entities.ChildToDirection
import org.careerseekers.cseventsservice.enums.UsersRoles
import org.careerseekers.cseventsservice.io.BasicSuccessfulResponse
import org.careerseekers.cseventsservice.io.converters.extensions.toHttpResponse
import org.careerseekers.cseventsservice.services.ChildToDirectionService
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
@RequestMapping("/events-service/v1/childToDirection")
class ChildToDirectionController(private val service: ChildToDirectionService) {

    @GetMapping("/")
    fun getAll() = service.getAll().toHttpResponse()

    @GetMapping("/{id}")
    fun getById(@PathVariable id: Long) =
        service.getById(id, message = "Запись об участии в компетенции с ID $id не найдена.")!!.toHttpResponse()

    @GetMapping("/getByChildId/{id}")
    fun getByChildId(@PathVariable id: Long): BasicSuccessfulResponse<List<ChildToDirection>> {
        return service.getByChildId(id).toHttpResponse()
    }

    @GetMapping("/getByDirectionId/{id}")
    fun getByDirectionId(@PathVariable id: Long) = service.getByDirectionId(id).toHttpResponse()

    @PostMapping("/")
    @AccessUntil(
        until = "2025-10-15T23:59:59+03:00",
        allowedRoles = [UsersRoles.ADMIN],
        errorMessage = "Ой, кажется время записи на компетенции подошло к концу"
    )
    fun create(@RequestBody item: CreateChildWithDirectionDto) = service.create(item).toHttpResponse()

    @PatchMapping("/")
    @AccessUntil(
        until = "2025-10-15T23:59:59+03:00",
        allowedRoles = [UsersRoles.ADMIN, UsersRoles.EXPERT],
        errorMessage = "Ой, кажется время записи на компетенции подошло к концу"
    )
    fun update(@RequestBody item: UpdateChildToDirectionsDto) = service.update(item).toHttpResponse()

    @PatchMapping("/setTeacherInfo")
    fun setTeacherInfo(@RequestBody item: SetTeacherInfoDto) = service.setTeacherInfo(item).toHttpResponse()

    @PatchMapping("/clearTeacherInfo/{recordId}")
    fun clearTeacherInfo(@PathVariable recordId: Long) = service.clearTeacherInfo(recordId).toHttpResponse()

    @DeleteMapping("/{id}")
    @AccessUntil(
        until = "2025-10-15T23:59:59+03:00",
        allowedRoles = [UsersRoles.ADMIN],
        errorMessage = "Ой, кажется время записи на компетенции подошло к концу"
    )
    fun deleteById(@PathVariable id: Long) = service.deleteById(id).toHttpResponse()

    @DeleteMapping("/deleteByChildId/{id}")
    @AccessUntil(
        until = "2025-10-15T23:59:59+03:00",
        allowedRoles = [UsersRoles.ADMIN],
        errorMessage = "Ой, кажется время записи на компетенции подошло к концу"
    )
    fun deleteByChildId(@PathVariable id: Long) = service.deleteByChildId(id).toHttpResponse()

    @PreAuthorize("hasAuthority('ADMIN')")
    @DeleteMapping("/")
    @AccessUntil(
        until = "2025-10-15T23:59:59+03:00",
        allowedRoles = [UsersRoles.ADMIN],
        errorMessage = "Ой, кажется время записи на компетенции подошло к концу"
    )
    fun deleteAll() = service.deleteAll().toHttpResponse()
}