package org.careerseekers.cseventsservice.services.reports

import com.careerseekers.grpc.children.ChildrenServiceGrpc
import com.careerseekers.grpc.children.Empty
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.coroutineScope
import net.devh.boot.grpc.client.inject.GrpcClient
import org.apache.poi.ss.usermodel.Row
import org.careerseekers.cseventsservice.entities.ChildToDirection
import org.careerseekers.cseventsservice.enums.DirectionAgeCategory.Companion.getAgeAlias
import org.careerseekers.cseventsservice.services.ChildToDirectionService
import org.careerseekers.cseventsservice.utils.ExcelReportBuilder
import org.springframework.stereotype.Service
import java.io.ByteArrayInputStream

@Service
class AllChildrenReportService(
    private val childToDirectionService: ChildToDirectionService,
) {
    @GrpcClient("users-service")
    lateinit var rpcChildrenService: ChildrenServiceGrpc.ChildrenServiceBlockingStub

    private data class ReportRow(
        val childName: String,

        val userName: String,
        val userEmail: String,
        val userPhone: String,

        val mentorName: String?,
        val mentorEmail: String?,
        val mentorPhone: String?,

        val directionName: String,
        val directionAgeCategory: String,
    ) : ExcelReportBuilder.ReportRows {
        override fun fillRow(row: Row) {
            row.createCell(0).setCellValue(childName)
            row.createCell(1).setCellValue(userName)
            row.createCell(2).setCellValue(userEmail)
            row.createCell(3).setCellValue(userPhone)
            row.createCell(4).setCellValue(mentorName)
            row.createCell(5).setCellValue(mentorEmail)
            row.createCell(6).setCellValue(mentorPhone)
            row.createCell(7).setCellValue(directionName)
            row.createCell(8).setCellValue(directionAgeCategory)
        }
    }

    suspend fun createReport(): ByteArrayInputStream = coroutineScope {
        val removableRecords = mutableListOf<ChildToDirection>()
        val records = childToDirectionService.getAll()
            .sortedWith(compareBy({ it.direction.name }, { it.directionAgeCategory.ageCategory }))

        val allChildren = rpcChildrenService
            .getAllFull(Empty.newBuilder().build())
            .childrenList
            .associateBy { it.id }

        val deferredRows = records.mapNotNull { record ->
            val child = allChildren[record.childId] ?: run {
                removableRecords.add(record)
                return@mapNotNull null
            }

            async(Dispatchers.IO) {
                val skipMentor = !child.hasMentor() || child.user.id == child.mentor.id
                ReportRow(
                    childName = "${child.lastName} ${child.firstName} ${child.patronymic}",
                    userName = "${child.user.lastName} ${child.user.firstName} ${child.user.patronymic}",
                    userEmail = child.user.email,
                    userPhone = child.user.mobileNumber,
                    mentorName = if (!skipMentor) "${child.mentor.lastName} ${child.mentor.firstName} ${child.mentor.patronymic}" else "—",
                    mentorEmail = if (!skipMentor) child.mentor.email else "—",
                    mentorPhone = if (!skipMentor) child.mentor.mobileNumber else "—",
                    directionName = record.direction.name,
                    directionAgeCategory = record.directionAgeCategory.ageCategory.getAgeAlias()
                )
            }
        }

        val rows = deferredRows.awaitAll().also {
            childToDirectionService.deleteAllByIds(removableRecords.map { it.id })
        }

        return@coroutineScope createExcelFile(rows)
    }

    private fun createExcelFile(rows: List<ReportRow>): ByteArrayInputStream {
        val headers = listOf(
            "ФИО ребёнка",
            "ФИО Родителя",
            "Эл. почта родителя",
            "Номер телефона родителя",
            "ФИО Наставника",
            "Эл. почта наставника",
            "Номер телефона наставника",
            "Компетенция",
            "Возрастная категория",
        )
        return ExcelReportBuilder.build(rows, headers, "Отчёт об участниках чемпионата")
    }
}