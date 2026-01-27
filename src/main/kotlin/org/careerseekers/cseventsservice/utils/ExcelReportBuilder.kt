package org.careerseekers.cseventsservice.utils

import org.apache.poi.ss.usermodel.HorizontalAlignment
import org.apache.poi.ss.usermodel.Row
import org.apache.poi.ss.usermodel.Sheet
import org.apache.poi.ss.usermodel.VerticalAlignment
import org.apache.poi.xssf.usermodel.XSSFWorkbook
import java.io.ByteArrayInputStream
import java.io.ByteArrayOutputStream

object ExcelReportBuilder {
    fun interface ReportRows {
        fun fillRow(row: Row)
    }

    fun build(rows: List<ReportRows>, headers: List<String>, sheetName: String? = "Отчёт"): ByteArrayInputStream {
        val workbook = XSSFWorkbook()
        val sheet = workbook.createSheet(sheetName)

        val headerFont = workbook.createFont().apply {
            bold = true
            fontHeightInPoints = 12
            fontName = "Arial"
        }

        val headerCellStyle = workbook.createCellStyle().apply {
            setFont(headerFont)
            alignment = HorizontalAlignment.CENTER
            verticalAlignment = VerticalAlignment.CENTER
        }

        val header = sheet.createRow(0)
        headers.forEachIndexed { index, title ->
            val cell = header.createCell(index)
            cell.setCellValue(title)
            cell.cellStyle = headerCellStyle
        }

        rows.forEachIndexed { rowIndex, reportRow ->
            val row = sheet.createRow(rowIndex + 1)
            reportRow.fillRow(row)
        }

        setColumnWidthsByHeaders(sheet, headers)

        val outputStream = ByteArrayOutputStream()
        workbook.write(outputStream)
        workbook.close()
        return ByteArrayInputStream(outputStream.toByteArray())
    }

    private fun setColumnWidthsByHeaders(sheet: Sheet, headers: List<String>) {
        headers.forEachIndexed { index, headerText ->
            val baseWidth = (headerText.length * 300 + 1000).coerceAtLeast(2000).coerceAtMost(8000)
            sheet.setColumnWidth(index, baseWidth)
        }
    }
}