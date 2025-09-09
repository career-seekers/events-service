package org.careerseekers.cseventsservice.dto.docs

import org.careerseekers.cseventsservice.dto.DtoClass
import org.careerseekers.cseventsservice.enums.FileTypes

data class UpdateDirectionDocumentDto(
    val id: Long,
    val documentType: FileTypes,
) : DtoClass
