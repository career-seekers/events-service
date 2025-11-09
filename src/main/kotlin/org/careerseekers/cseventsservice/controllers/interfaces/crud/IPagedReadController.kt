package org.careerseekers.cseventsservice.controllers.interfaces.crud

import org.careerseekers.cseventsservice.controllers.interfaces.BasicRestController
import org.careerseekers.cseventsservice.dto.FilterDtoClass
import org.careerseekers.cseventsservice.io.BasicSuccessfulResponse
import org.careerseekers.cseventsservice.services.interfaces.crud.IPagedReadService
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable

interface IPagedReadController<T, ID, Filters: FilterDtoClass> : BasicRestController {
    override val service: IPagedReadService<T, ID, Filters>

    fun getAll(filters: Filters, pageable: Pageable): BasicSuccessfulResponse<Page<T>>
    fun getById(id: ID): BasicSuccessfulResponse<T>
}