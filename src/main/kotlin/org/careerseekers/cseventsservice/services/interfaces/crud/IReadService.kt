package org.careerseekers.cseventsservice.services.interfaces.crud

import org.careerseekers.cseventsservice.exceptions.NotFoundException
import org.careerseekers.cseventsservice.services.interfaces.BasicApiService

interface IReadService<T, ID> : BasicApiService<T, ID> {
    fun getAll(): List<T> = repository.findAll()
    fun getById(id: ID?, throwable: Boolean = true, message: String = "Object with id $id not found."): T? {
        if (id == null) {
            if (throwable) {
                throw NotFoundException("ID cannot be null.")
            }
            return null
        }

        val o = repository.findById(id)
        if (throwable && !o.isPresent) {
            throw NotFoundException(message)
        }
        return if (!o.isPresent) null else o.get()
    }
}