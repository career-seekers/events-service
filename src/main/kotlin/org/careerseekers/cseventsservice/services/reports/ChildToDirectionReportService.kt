package org.careerseekers.cseventsservice.services.reports

import com.careerseekers.grpc.users.ChildId
import com.careerseekers.grpc.users.UsersServiceGrpc
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.coroutineScope
import net.devh.boot.grpc.client.inject.GrpcClient
import org.apache.poi.ss.usermodel.Row
import org.careerseekers.cseventsservice.enums.DirectionAgeCategory.Companion.getAgeAlias
import org.careerseekers.cseventsservice.enums.QueueStatus.Companion.getAlias
import org.careerseekers.cseventsservice.services.ChildToDirectionService
import org.careerseekers.cseventsservice.services.DirectionsService
import org.careerseekers.cseventsservice.utils.ExcelReportBuilder
import org.springframework.stereotype.Service
import java.io.ByteArrayInputStream

@Service
class ChildToDirectionReportService(
    private val directionsService: DirectionsService,
    private val childToDirectionService: ChildToDirectionService,
) {
    private data class ReportRow(
        val childName: String,
        val parentName: String,
        val email: String,
        val mobileNumber: String,
        val tgLink: String,
        val mentorName: String,
        val mentorEmail: String,
        val mentorPhoneNumber: String,
        val mentorTgLink: String,
        val schoolName: String,
        val trainingGroundName: String,
        val queueStatus: String,
        val ageCategory: String,
    ) : ExcelReportBuilder.ReportRows {
        override fun fillRow(row: Row) {
            row.createCell(0).setCellValue(childName)
            row.createCell(1).setCellValue(parentName)
            row.createCell(2).setCellValue(email)
            row.createCell(3).setCellValue(mobileNumber)
            row.createCell(4).setCellValue(tgLink)
            row.createCell(5).setCellValue(mentorName)
            row.createCell(6).setCellValue(mentorEmail)
            row.createCell(7).setCellValue(mentorPhoneNumber)
            row.createCell(8).setCellValue(mentorTgLink)
            row.createCell(9).setCellValue(schoolName)
            row.createCell(10).setCellValue(trainingGroundName)
            row.createCell(11).setCellValue(queueStatus)
            row.createCell(12).setCellValue(ageCategory)
        }
    }

    @GrpcClient("users-service")
    lateinit var usersServiceStub: UsersServiceGrpc.UsersServiceBlockingStub

    suspend fun createChildRecordsRapport(directionId: Long): ByteArrayInputStream = coroutineScope {
        val direction = directionsService.getById(directionId, message = "Компетенция с ID $directionId не найдена.")!!
        val records = childToDirectionService.getByDirectionId(direction.id)
            .sortedBy { it.directionAgeCategory.ageCategory }

        val deferredRows = records.map { record ->
            async(Dispatchers.IO) {
                val res = usersServiceStub.getChildWithUser(ChildId.newBuilder().setId(record.childId).build())
                val isSameMentorAsUser = res.user == res.mentor
                ReportRow(
                    childName = "${res.lastName} ${res.firstName} ${res.patronymic}",

                    parentName = "${res.user.lastName} ${res.user.firstName} ${res.user.patronymic}",
                    email = res.user.email,
                    mobileNumber = res.user.mobileNumber,
                    tgLink = res.user.tgLink,

                    mentorName = if (isSameMentorAsUser) "—" else "${res.mentor.lastName} ${res.mentor.firstName} ${res.mentor.patronymic}",
                    mentorEmail = if (isSameMentorAsUser) "—" else res.mentor.email,
                    mentorPhoneNumber = if (isSameMentorAsUser) "—" else res.mentor.mobileNumber,
                    mentorTgLink = if (isSameMentorAsUser) "—" else res.mentor.tgLink,

                    schoolName = res.schoolName,
                    trainingGroundName = res.trainingGroundName,
                    queueStatus = record.queueStatus.getAlias(),
                    ageCategory = record.directionAgeCategory.ageCategory.getAgeAlias()
                )
            }
        }

        val rows = deferredRows.awaitAll()

        createChildRecordsExcel(rows)
    }

    private fun createChildRecordsExcel(rows: List<ReportRow>): ByteArrayInputStream {
        val headers = listOf(
            "ФИО ребёнка",
            "ФИО Родителя",
            "Эл. почта родителя",
            "Номер телефона родителя",
            "Ссылка на телеграм аккаунт родителя",
            "ФИО Наставника",
            "Эл. почта наставника",
            "Номер телефона наставника",
            "Ссылка на телеграм аккаунт наставника",
            "Школа",
            "Площадка подготовки",
            "Статус записи",
            "Возрастная категория",
        )
        return ExcelReportBuilder.build(rows, headers, "Отчёт об участниках компетенции")
    }
}