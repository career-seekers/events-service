package org.careerseekers.cseventsservice.services.interfaces

import org.careerseekers.cseventsservice.dto.FilterDtoClass
import org.careerseekers.cseventsservice.services.interfaces.crud.ICreateService
import org.careerseekers.cseventsservice.services.interfaces.crud.IDeleteService
import org.careerseekers.cseventsservice.services.interfaces.crud.IPagedReadService
import org.careerseekers.cseventsservice.services.interfaces.crud.IUpdateService
import org.springframework.transaction.annotation.Transactional

interface PagedCrudService<T, ID, CrDTO, UpDTO, Filters: FilterDtoClass> :
    IPagedReadService<T, ID, Filters>,
    ICreateService<T, ID, CrDTO>,
    IUpdateService<T, ID, UpDTO>,
    IDeleteService<T, ID>
{
    @Transactional
    fun createAll(items: List<CrDTO>): Any
}