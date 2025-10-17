package org.careerseekers.cseventsservice.controllers

import org.careerseekers.cseventsservice.services.RapportsService
import org.springframework.core.io.InputStreamResource
import org.springframework.http.HttpHeaders
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/events-service/v1/rapports/")
class RapportsController(private val service: RapportsService) {

    @GetMapping("/getChildRecords/{directionId}")
    fun getChildRecords(@PathVariable directionId: Long): ResponseEntity<InputStreamResource?> {
        val resource = InputStreamResource(service.createChildRecordsRapport(directionId))

        return ResponseEntity.ok()
            .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=отчет_о_записях_на_компетенцию.xlsx")
            .contentType(MediaType.parseMediaType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet"))
            .body(resource)
    }
}