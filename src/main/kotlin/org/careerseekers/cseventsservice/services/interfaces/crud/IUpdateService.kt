package org.careerseekers.cseventsservice.services.interfaces.crud

import org.careerseekers.cseventsservice.services.interfaces.BasicApiService
import org.springframework.transaction.annotation.Transactional

interface IUpdateService<T, ID, UpDTO> : BasicApiService<T, ID> {
    @Transactional
    fun update(item: UpDTO): Any
}