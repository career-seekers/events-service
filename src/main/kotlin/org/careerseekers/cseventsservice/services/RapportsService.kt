package org.careerseekers.cseventsservice.services

import com.careerseekers.grpc.users.ChildId
import com.careerseekers.grpc.users.UsersServiceGrpc
import io.grpc.Status
import io.grpc.StatusRuntimeException
import net.devh.boot.grpc.client.inject.GrpcClient
import org.apache.poi.ss.usermodel.HorizontalAlignment
import org.apache.poi.ss.usermodel.VerticalAlignment
import org.apache.poi.xssf.usermodel.XSSFWorkbook
import org.careerseekers.cseventsservice.dto.rapports.ChildRecordsRapportDto
import org.careerseekers.cseventsservice.enums.DirectionAgeCategory.Companion.getAgeAlias
import org.careerseekers.cseventsservice.enums.QueueStatus.Companion.getAlias
import org.careerseekers.cseventsservice.exceptions.GrpcServiceUnavailableException
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.io.ByteArrayInputStream
import java.io.ByteArrayOutputStream

@Service
class RapportsService(
    private val directionsService: DirectionsService,
    private val childToDirectionService: ChildToDirectionService,
) {

    @GrpcClient("users-service")
    lateinit var usersServiceStub: UsersServiceGrpc.UsersServiceBlockingStub

    @Transactional
    fun createChildRecordsRapport(directionId: Long): ByteArrayInputStream {
        val direction = directionsService.getById(directionId, message = "Компетенция с ID $directionId не найдена.")!!

        val records = mutableListOf<ChildRecordsRapportDto>()
        childToDirectionService.getByDirectionId(direction.id).sortedBy { it.directionAgeCategory.ageCategory }.forEach { record ->
            try {
                val res = usersServiceStub.getChildWithUser(ChildId.newBuilder().setId(record.childId).build())
                records.addLast(
                    ChildRecordsRapportDto(
                        childName = "${res.lastName} ${res.firstName} ${res.patronymic}",
                        parentName = "${res.user.lastName} ${res.user.firstName} ${res.user.patronymic}",
                        email = res.user.email,
                        mobileNumber = res.user.mobileNumber,
                        tgLink = res.user.tgLink,
                        schoolName = res.schoolName,
                        trainingGroundName = res.trainingGroundName,
                        queueStatus = record.queueStatus.getAlias(),
                        ageCategory = record.directionAgeCategory.ageCategory.getAgeAlias()
                    )
                )
            } catch (e: StatusRuntimeException) {
                when (e.status.code) {
                    Status.Code.NOT_FOUND -> records.addLast(ChildRecordsRapportDto(
                        childName = "Участник удален из системы",
                        parentName = "Данную запись необходимо удалить в личном кабинете эксперта",
                        email = "—",
                        mobileNumber = "—",
                        tgLink = "—",
                        schoolName = "—",
                        trainingGroundName = "—",
                        queueStatus = "Удален из системы",
                        ageCategory = record.directionAgeCategory.ageCategory.getAgeAlias()
                    ))
                    Status.Code.UNAVAILABLE -> throw GrpcServiceUnavailableException("GRPC сервис пользователей недоступен в данный момент.")

                    else -> throw e
                }
            }
        }

        return createChildRecordsExcel(records)
    }

    private fun createChildRecordsExcel(records: List<ChildRecordsRapportDto>): ByteArrayInputStream {
        val workbook = XSSFWorkbook()
        val sheet = workbook.createSheet("Записи детей на компетенцию")

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
        val headers = listOf(
            "ФИО ребёнка",
            "ФИО Родителя",
            "Эл. почта",
            "Номер телефона",
            "Ссылка на телеграм аккаунт",
            "Школа",
            "Площадка подготовки",
            "Статус записи",
            "Возрастная категория",
        )
        headers.forEachIndexed { index, title ->
            val cell = header.createCell(index)
            cell.setCellValue(title)
            cell.cellStyle = headerCellStyle
            sheet.autoSizeColumn(index)
        }

        records.forEachIndexed { rowIndex, record ->
            val row = sheet.createRow(rowIndex + 1)
            row.createCell(0).setCellValue(record.childName)
            row.createCell(1).setCellValue(record.parentName)
            row.createCell(2).setCellValue(record.email)
            row.createCell(3).setCellValue(record.mobileNumber)
            row.createCell(4).setCellValue(record.tgLink)
            row.createCell(5).setCellValue(record.schoolName)
            row.createCell(6).setCellValue(record.trainingGroundName)
            row.createCell(7).setCellValue(record.queueStatus)
            row.createCell(8).setCellValue(record.ageCategory)
        }

        val outputStream = ByteArrayOutputStream()
        workbook.write(outputStream)
        workbook.close()
        return ByteArrayInputStream(outputStream.toByteArray())
    }
}