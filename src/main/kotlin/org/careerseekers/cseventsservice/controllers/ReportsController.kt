package org.careerseekers.cseventsservice.controllers

import org.careerseekers.cseventsservice.services.reports.AllChildrenReportService
import org.careerseekers.cseventsservice.services.reports.ChildToDirectionReportService
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
class ReportsController(
    private val childToDirectionReportService: ChildToDirectionReportService,
    private val allChildrenReportService: AllChildrenReportService,
) {

    @GetMapping("/getChildRecords/{directionId}")
    fun getChildRecords(@PathVariable directionId: Long): ResponseEntity<InputStreamResource?> {
        val resource = InputStreamResource(childToDirectionReportService.createChildRecordsRapport(directionId))

        return ResponseEntity.ok()
            .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=competition_records_report.xlsx")
            .contentType(MediaType.parseMediaType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet"))
            .body(resource)
    }

    @GetMapping("/getChildrenReport")
    suspend fun getChildrenReport(): ResponseEntity<InputStreamResource?> {
        val resource = InputStreamResource(allChildrenReportService.createReport())

        return ResponseEntity.ok()
            .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=all_children_report.xlsx")
            .contentType(MediaType.parseMediaType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet"))
            .body(resource)
    }
}