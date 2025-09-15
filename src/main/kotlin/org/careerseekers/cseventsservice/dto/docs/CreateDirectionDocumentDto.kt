package org.careerseekers.cseventsservice.dto.docs

import org.careerseekers.cseventsservice.dto.DtoClass
import org.careerseekers.cseventsservice.entities.Directions
import org.careerseekers.cseventsservice.enums.DirectionAgeCategory
import org.careerseekers.cseventsservice.enums.FileTypes
import org.springframework.web.multipart.MultipartFile
import java.time.LocalDateTime

data class CreateDirectionDocumentDto(
    val documentType: FileTypes,
    val document: MultipartFile,
    val ageCategory: DirectionAgeCategory,
    val documentId: Long? = null,
    val createdAt: LocalDateTime? = null,
    val userId: Long,
    val directionId: Long,
    val direction: Directions? = null,
) : DtoClass
