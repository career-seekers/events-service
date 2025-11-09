package org.careerseekers.cseventsservice.controllers.interfaces

import org.careerseekers.cseventsservice.controllers.interfaces.crud.ICreateController
import org.careerseekers.cseventsservice.controllers.interfaces.crud.IDeleteController
import org.careerseekers.cseventsservice.controllers.interfaces.crud.IPagedReadController
import org.careerseekers.cseventsservice.controllers.interfaces.crud.IUpdateController
import org.careerseekers.cseventsservice.dto.FilterDtoClass
import org.careerseekers.cseventsservice.io.BasicSuccessfulResponse
import org.careerseekers.cseventsservice.services.interfaces.PagedCrudService

interface PagedCrudController<T, ID, CrDTO, UpDTO, Filters : FilterDtoClass>
    : IPagedReadController<T, ID, Filters>,
    ICreateController<T, ID, CrDTO>,
    IUpdateController<T, ID, UpDTO>,
    IDeleteController<T, ID> {
    override val service: PagedCrudService<T, ID, CrDTO, UpDTO, Filters>

    fun createAll(items: List<CrDTO>): BasicSuccessfulResponse<*>
}