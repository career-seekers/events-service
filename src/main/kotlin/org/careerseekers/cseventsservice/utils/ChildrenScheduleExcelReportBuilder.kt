package org.careerseekers.cseventsservice.utils

import com.careerseekers.grpc.children.ShortChild
import org.apache.poi.ss.usermodel.HorizontalAlignment
import org.apache.poi.ss.usermodel.VerticalAlignment
import org.apache.poi.xssf.usermodel.XSSFWorkbook
import org.careerseekers.cseventsservice.entities.DirectionAgeCategories
import org.careerseekers.cseventsservice.entities.Directions
import org.careerseekers.cseventsservice.enums.DirectionAgeCategory
import org.careerseekers.cseventsservice.utils.timefold.ChildDirectionAssignment
import org.careerseekers.cseventsservice.utils.timefold.Slot
import java.io.ByteArrayInputStream
import java.io.ByteArrayOutputStream

object ChildrenScheduleExcelReportBuilder {
    fun buildScheduleReport(
        slots: List<Slot>,
        assignments: List<ChildDirectionAssignment>,
        children: List<ShortChild>,
        directions: List<Directions>,
        sheetName: String = "Расписание"
    ): ByteArrayInputStream {
        val workbook = XSSFWorkbook()
        val sheet = workbook.createSheet(sheetName)

        val directionById = directions.associateBy { it.id }

        val ageCategoryById: Map<Long, DirectionAgeCategories> =
            directions
                .flatMap { dir -> dir.ageCategories.orEmpty() }
                .associateBy { it.id }

        val assignmentsByChildId: Map<Long, List<ChildDirectionAssignment>> =
            assignments
                .filter { it.childId != null }
                .groupBy { it.childId!! }


        data class SlotHeader(
            val slot: Slot,
            val directionName: String,
            val ageAlias: String,
            val flow: Int
        ) {
            val headerTitle: String
                get() = "$directionName—$ageAlias—$flow"
        }

        val slotHeaders: List<SlotHeader> = slots.map { slot ->
            val direction = directionById[slot.directionId]
                ?: error("Direction not found for id=${slot.directionId}")

            val ageCategory = ageCategoryById[slot.ageCategoryId]
                ?: error("Age category not found for id=${slot.ageCategoryId}")

            val ageAlias = ageCategory.ageCategory.let {
                DirectionAgeCategory.run { it.getAgeAlias() }
            }

            SlotHeader(
                slot = slot,
                directionName = direction.name,
                ageAlias = ageAlias,
                flow = slot.flow
            )
        }.sortedWith(
            compareBy({ it.directionName }, { it.ageAlias }, { it.flow })
        )

        val slotIdToColumnIndex: Map<Long, Int> =
            slotHeaders.mapIndexed { index, sh -> sh.slot.id to (index + 1) /*+1 т.к. 0 = ФИО*/ }.toMap()

        val headerFont = workbook.createFont().apply {
            bold = true
            fontHeightInPoints = 12
            fontName = "Arial"
        }

        val headerCellStyle = workbook.createCellStyle().apply {
            setFont(headerFont)
            alignment = HorizontalAlignment.CENTER
            verticalAlignment = VerticalAlignment.CENTER
            wrapText = true
        }

        val defaultCellStyle = workbook.createCellStyle().apply {
            alignment = HorizontalAlignment.CENTER
            verticalAlignment = VerticalAlignment.CENTER
        }

        val headerRow = sheet.createRow(0)

        headerRow.createCell(0).apply {
            setCellValue("ФИО ребёнка")
            cellStyle = headerCellStyle
        }

        slotHeaders.forEachIndexed { index, sh ->
            val cell = headerRow.createCell(index + 1)
            cell.setCellValue(sh.headerTitle)
            cell.cellStyle = headerCellStyle
        }

        var currentRowIndex = 1

        val childrenWithAssignments: List<ShortChild> =
            children.filter { child -> assignmentsByChildId.containsKey(child.id) }

        for (child in childrenWithAssignments) {
            val row = sheet.createRow(currentRowIndex++)

            val fullName = listOfNotNull(
                child.lastName,
                child.firstName,
                child.patronymic
            ).joinToString(" ")
            row.createCell(0).apply {
                setCellValue(fullName)
                cellStyle = defaultCellStyle
            }

            val childAssignments = assignmentsByChildId[child.id] ?: emptyList()

            for (assignment in childAssignments) {
                val slot = assignment.slot ?: continue
                val colIndex = slotIdToColumnIndex[slot.id] ?: continue

                val cell = row.getCell(colIndex) ?: row.createCell(colIndex)
                cell.setCellValue(1.0)
                cell.cellStyle = defaultCellStyle
            }
        }

        val totalColumns = slotHeaders.size + 1
        for (i in 0 until totalColumns) {
            sheet.autoSizeColumn(i)
        }

        val outputStream = ByteArrayOutputStream()
        workbook.write(outputStream)
        workbook.close()
        return ByteArrayInputStream(outputStream.toByteArray())
    }
}