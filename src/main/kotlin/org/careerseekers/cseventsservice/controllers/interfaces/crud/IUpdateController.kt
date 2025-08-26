package org.careerseekers.cseventsservice.controllers.interfaces.crud

import org.careerseekers.cseventsservice.controllers.interfaces.BasicRestController
import org.careerseekers.cseventsservice.services.interfaces.crud.IUpdateService

interface IUpdateController<T, ID, UpDTO> : BasicRestController {
    override val service: IUpdateService<T, ID, UpDTO>

    fun update(item: UpDTO): Any
}