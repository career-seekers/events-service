package org.careerseekers.cseventsservice.services.reports

import com.careerseekers.grpc.children.ChildrenServiceGrpc
import com.careerseekers.grpc.children.Empty
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

    private suspend fun cleanupRecords(records: List<ChildToDirection>) = coroutineScope {
        records.filter { record ->
            rpcChildrenService.getAllFull(Empty.newBuilder().build())
                .childrenList.none { it.id == record.childId }
        }.forEach { recordToDelete ->
            childToDirectionService.deleteById(recordToDelete.id)
        }
    }

    suspend fun createReport(): ByteArrayInputStream = coroutineScope {
        val allRecords = childToDirectionService.getAll()
        val allChildren = rpcChildrenService
            .getAllFull(Empty.newBuilder().build())
            .childrenList
            .associateBy { it.id }

        cleanupRecords(allRecords)

        val validRecords = allRecords.filter { allChildren.containsKey(it.childId) }
        val sortedRecords = validRecords.sortedWith(compareBy({ it.direction.name }, { it.directionAgeCategory.ageCategory }))
        val rows = mutableListOf<ReportRow>()

        for (record in sortedRecords) {
            val child = allChildren[record.childId]!!
            val skipMentor = !child.hasMentor() || child.user.id == child.mentor.id
            rows.add(
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
            )
        }

        createExcelFile(rows)
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