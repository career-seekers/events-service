package org.careerseekers.cseventsservice.controllers.interfaces.crud

import org.careerseekers.cseventsservice.services.interfaces.crud.IDeleteService
import org.careerseekers.cseventsservice.controllers.interfaces.BasicRestController

interface IDeleteController<T, ID> : BasicRestController {
    override val service : IDeleteService<T, ID>

    fun deleteAll(): Any
    fun deleteById(id : ID): Any
}