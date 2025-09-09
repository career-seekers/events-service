package org.careerseekers.cseventsservice.mappers

import org.careerseekers.cseventsservice.dto.docs.CreateDirectionDocumentDto
import org.careerseekers.cseventsservice.entities.DirectionDocuments
import org.mapstruct.Mapper

@Mapper(componentModel = "spring")
interface DirectionDocumentsMapper {
    fun directionDocsFromDto(o: CreateDirectionDocumentDto): DirectionDocuments
}