package org.careerseekers.cseventsservice.dto.docs

import org.careerseekers.cseventsservice.dto.DtoClass
import org.careerseekers.cseventsservice.enums.DirectionDocumentsTypes

data class UpdateDirectionDocumentDto(
    val id: Long,
    val documentType: DirectionDocumentsTypes,
) : DtoClass
