package org.careerseekers.cseventsservice.controllers.interfaces.crud

import org.careerseekers.cseventsservice.controllers.interfaces.BasicRestController
import org.careerseekers.cseventsservice.services.interfaces.crud.ICreateService

interface ICreateController<T, ID, CrDTO> : BasicRestController {
    override val service: ICreateService<T, ID, CrDTO>

    fun create(item: CrDTO): Any
}