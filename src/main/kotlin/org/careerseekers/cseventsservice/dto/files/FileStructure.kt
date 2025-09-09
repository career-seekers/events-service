package org.careerseekers.cseventsservice.dto.files

import org.careerseekers.cseventsservice.dto.DtoClass
import org.careerseekers.cseventsservice.enums.FileTypes
import java.time.LocalDateTime

data class FileStructure(
    val id: Long,
    val originalFilename: String,
    val storedFilename: String,
    val contentType: String,
    val fileType: FileTypes,
    val filePath: String,
    val createdAt: LocalDateTime,
    val updatedAt: LocalDateTime,
) : DtoClass