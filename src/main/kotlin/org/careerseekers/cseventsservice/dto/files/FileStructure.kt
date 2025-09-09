package org.careerseekers.cseventsservice.dto.files

import org.careerseekers.cseventsservice.dto.DtoClass
import org.careerseekers.cseventsservice.enums.FileTypes

data class FileStructure(
    val id: Long,
    val originalFilename: String,
    val storedFilename: String,
    val contentType: String,
    val fileType: FileTypes,
    val filePath: String,
) : DtoClass