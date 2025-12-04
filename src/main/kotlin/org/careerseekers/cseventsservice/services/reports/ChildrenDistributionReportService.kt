package org.careerseekers.cseventsservice.services.reports

import com.careerseekers.grpc.children.ChildrenServiceGrpc
import com.careerseekers.grpc.children.Empty
import com.careerseekers.grpc.children.ShortChild
import net.devh.boot.grpc.client.inject.GrpcClient
import org.careerseekers.cseventsservice.services.DirectionsService
import org.careerseekers.cseventsservice.utils.ChildrenScheduleExcelReportBuilder
import org.careerseekers.cseventsservice.utils.timefold.ChildDirectionAssignment
import org.careerseekers.cseventsservice.utils.timefold.Slot
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.io.ByteArrayInputStream
import java.nio.file.Files
import java.nio.file.Path
import java.nio.file.Paths

@Service
class ChildrenDistributionReportService(
    private val directionsService: DirectionsService,
) {

    @GrpcClient("users-service")
    lateinit var rpcChildrenService: ChildrenServiceGrpc.ChildrenServiceBlockingStub

    @Transactional(readOnly = true)
    fun createReport(slots: List<Slot>, assignments: List<ChildDirectionAssignment>) { emptyList<ShortChild>()
        val allChildren = rpcChildrenService.getAll(Empty.newBuilder().build()).childrenList

        val excelStream = ChildrenScheduleExcelReportBuilder.buildScheduleReport(
            slots = slots,
            assignments = assignments,
            children = allChildren,
            directions = directionsService.getAll()
        )

        saveExcelToFile(excelStream, Paths.get("/tmp/schedule.xlsx"))
    }

    private fun saveExcelToFile(
        excelStream: ByteArrayInputStream,
        path: Path
    ) {
        Files.newOutputStream(path).use { out ->
            excelStream.use { input ->
                input.copyTo(out)
            }
        }
    }
}