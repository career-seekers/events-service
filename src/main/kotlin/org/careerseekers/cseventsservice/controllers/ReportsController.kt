package org.careerseekers.cseventsservice.controllers

import kotlinx.coroutines.runBlocking
import org.careerseekers.cseventsservice.services.reports.AllChildrenReportService
import org.careerseekers.cseventsservice.services.reports.ChildToDirectionReportService
import org.slf4j.LoggerFactory
import org.springframework.core.io.ByteArrayResource
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

    private val logger = LoggerFactory.getLogger(ReportsController::class.java)

    @GetMapping(
        "/getChildRecords/{directionId}",
        produces = ["application/vnd.openxmlformats-officedocument.spreadsheetml.sheet"]
    )
    fun getChildRecords(@PathVariable directionId: Long): ResponseEntity<ByteArrayResource?> {
        val inputStream = runBlocking {
            childToDirectionReportService.createChildRecordsRapport(directionId)
        }
        val bytes = inputStream.readBytes()
        val resource = ByteArrayResource(bytes)

        logger.info("Step 4, resource file: $resource")
        return ResponseEntity.ok()
            .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=competition_records_report.xlsx")
            .contentType(MediaType.parseMediaType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet"))
            .body(resource)
    }

    @GetMapping("/getChildrenReport")
     fun getChildrenReport(): ResponseEntity<ByteArrayResource?> {
        val inputStream = runBlocking {
            allChildrenReportService.createReport()
        }
        val bytes = inputStream.readBytes()
        val resource = ByteArrayResource(bytes)

        logger.info("Step 4, resource: $resource")

        return ResponseEntity.ok()
            .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=all_children_report.xlsx")
            .contentType(MediaType.parseMediaType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet"))
            .body(resource)
    }
}