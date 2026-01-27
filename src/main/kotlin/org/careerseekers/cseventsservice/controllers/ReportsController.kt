package org.careerseekers.cseventsservice.controllers

import kotlinx.coroutines.runBlocking
import org.careerseekers.cseventsservice.enums.EventTypes
import org.careerseekers.cseventsservice.services.reports.AllChildrenReportService
import org.careerseekers.cseventsservice.services.reports.ChildToDirectionReportService
import org.careerseekers.cseventsservice.services.reports.EventsReportService
import org.springframework.core.io.ByteArrayResource
import org.springframework.http.HttpHeaders
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/events-service/v1/rapports/")
class ReportsController(
    private val childToDirectionReportService: ChildToDirectionReportService,
    private val allChildrenReportService: AllChildrenReportService,
    private val eventsReportService: EventsReportService,
) {
    private val contentType = MediaType.parseMediaType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet")

    @GetMapping("/getChildRecords/{directionId}")
    fun getChildRecords(@PathVariable directionId: Long): ResponseEntity<ByteArrayResource?> {
        val inputStream = runBlocking {
            childToDirectionReportService.createChildRecordsRapport(directionId)
        }
        val bytes = inputStream.readBytes()
        val resource = ByteArrayResource(bytes)

        return ResponseEntity.ok()
            .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=competition_records_report.xlsx")
            .contentType(contentType)
            .body(resource)
    }

    @GetMapping("/getChildrenReport")
    fun getChildrenReport(): ResponseEntity<ByteArrayResource?> {
        val inputStream = runBlocking {
            allChildrenReportService.createReport()
        }
        val bytes = inputStream.readBytes()
        val resource = ByteArrayResource(bytes)

        return ResponseEntity.ok()
            .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=all_children_report.xlsx")
            .contentType(contentType)
            .body(resource)
    }

    @GetMapping("/getEventsReport")
    fun getEventsReport(@RequestParam eventType: EventTypes?): ResponseEntity<ByteArrayResource?> {
        val inputStream = runBlocking {
            eventsReportService.createReport(eventType)
        }
        val bytes = inputStream.readBytes()
        val resource = ByteArrayResource(bytes)

        return ResponseEntity.ok()
            .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=all_events_report.xlsx")
            .contentType(contentType)
            .body(resource)
    }

}