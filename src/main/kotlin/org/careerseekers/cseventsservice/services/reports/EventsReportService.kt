package org.careerseekers.cseventsservice.services.reports

import com.careerseekers.grpc.users.UsersServiceGrpc
import jakarta.persistence.criteria.Path
import kotlinx.coroutines.coroutineScope
import net.devh.boot.grpc.client.inject.GrpcClient
import org.apache.poi.ss.usermodel.Row
import org.careerseekers.cseventsservice.dto.UsersCacheDto.Companion.getName
import org.careerseekers.cseventsservice.entities.Events
import org.careerseekers.cseventsservice.enums.DirectionAgeCategory.Companion.getAgeAlias
import org.careerseekers.cseventsservice.enums.EventFormats.Companion.getAlias
import org.careerseekers.cseventsservice.enums.EventTypes
import org.careerseekers.cseventsservice.enums.EventTypes.Companion.getAlias
import org.careerseekers.cseventsservice.io.converters.extensions.rpc.toList
import org.careerseekers.cseventsservice.io.converters.extensions.rpc.toUserIds
import org.careerseekers.cseventsservice.repositories.EventsRepository
import org.careerseekers.cseventsservice.utils.ExcelReportBuilder
import org.careerseekers.cseventsservice.utils.mapParallel
import org.springframework.stereotype.Service
import java.io.ByteArrayInputStream
import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter


@Service
class EventsReportService(private val eventsRepository: EventsRepository) {

    @GrpcClient("users-service")
    lateinit var rpcUsersService: UsersServiceGrpc.UsersServiceBlockingStub

    private data class ReportRow(
        val directionName: String,
        val ageCategory: String,
        val eventType: String,
        val eventFormat: String,
        val eventVenue: String,
        val eventDate: ZonedDateTime,
        val eventTime: ZonedDateTime,
        val expertName: String,
        val shortDescription: String,
        val description: String,
    ) : ExcelReportBuilder.ReportRows {
        override fun fillRow(row: Row) {
            row.createCell(0).setCellValue(directionName)
            row.createCell(1).setCellValue(ageCategory)
            row.createCell(2).setCellValue(eventType)
            row.createCell(3).setCellValue(eventFormat)
            row.createCell(4).setCellValue(eventVenue)
            row.createCell(5).setCellValue(eventDate.format(DateTimeFormatter.ofPattern("dd.MM.yyyy")))
            row.createCell(6).setCellValue(eventDate.format(DateTimeFormatter.ofPattern("HH:mm")))
            row.createCell(7).setCellValue(expertName)
            row.createCell(8).setCellValue(shortDescription)
            row.createCell(9).setCellValue(description)
        }
    }

    suspend fun createReport(eventType: EventTypes?): ByteArrayInputStream = coroutineScope {
        val events = eventType?.let {
            eventsRepository.findAll { root, _, builder ->
                val eventTypePath: Path<EventTypes> = root["eventType"]
                builder.equal(eventTypePath, eventType)
            }
        } ?: eventsRepository.findAll().sortedWith(compareBy<Events> { it.eventType }.thenBy { it.directionName })

        val experts = rpcUsersService
            .getUsersByIds(
                events.map { it.directionExpertId }
                    .distinct()
                    .toUserIds()
            )
            .toList()
            .associateBy { it.id }

        val rows = events.mapParallel { event ->
            ReportRow(
                directionName = event.direction.name,
                ageCategory = event.directionAgeCategory.ageCategory.getAgeAlias(),
                eventType = event.eventType.getAlias(),
                eventFormat = event.eventFormat.getAlias(),
                eventVenue = event.eventVenue ?: "У этого события не назначено место проведения",
                eventDate = event.startDateTime,
                eventTime = event.startDateTime,
                expertName = experts[event.directionExpertId]?.getName()
                    ?: "Не удалось найти эксперта этого события",
                shortDescription = event.shortDescription,
                description = event.description ?: "Описания пока еще нет"
            )
        }

        return@coroutineScope createExcelFile(rows)
    }

    private fun createExcelFile(rows: List<ReportRow>): ByteArrayInputStream {
        val headers = listOf(
            "Название компетенции",
            "Возрастная категория",
            "Тип события",
            "Формат проведения",
            "Место проведения / Ссылка",
            "Дата начала",
            "Время начала",
            "ФИО Эксперта",
            "Краткое описание",
            "Полное описание",
        )

        return ExcelReportBuilder.build(rows, headers, "Отчёт о событиях чемпионата")
    }
}