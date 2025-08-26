package org.careerseekers.cseventsservice.controllers.interfaces

import org.careerseekers.cseventsservice.controllers.interfaces.crud.ICreateController
import org.careerseekers.cseventsservice.controllers.interfaces.crud.IDeleteController
import org.careerseekers.cseventsservice.controllers.interfaces.crud.IReadController
import org.careerseekers.cseventsservice.controllers.interfaces.crud.IUpdateController
import org.careerseekers.cseventsservice.io.BasicSuccessfulResponse
import org.careerseekers.cseventsservice.services.interfaces.CrudService

interface CrudController<T, ID, CrDTO, UpDTO>
    : IReadController<T, ID>,
    ICreateController<T, ID, CrDTO>,
    IUpdateController<T, ID, UpDTO>,
    IDeleteController<T, ID> {
    override val service: CrudService<T, ID, CrDTO, UpDTO>

    fun createAll(items: List<CrDTO>): BasicSuccessfulResponse<*>
}