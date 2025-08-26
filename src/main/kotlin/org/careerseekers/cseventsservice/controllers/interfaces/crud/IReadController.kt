package org.careerseekers.cseventsservice.controllers.interfaces.crud

import org.careerseekers.cseventsservice.controllers.interfaces.BasicRestController
import org.careerseekers.cseventsservice.io.BasicSuccessfulResponse
import org.careerseekers.cseventsservice.services.interfaces.crud.IReadService

interface IReadController<T, ID> : BasicRestController {
    override val service: IReadService<T, ID>

    fun getAll(): BasicSuccessfulResponse<List<T>>
    fun getById(id: ID): BasicSuccessfulResponse<T>
}