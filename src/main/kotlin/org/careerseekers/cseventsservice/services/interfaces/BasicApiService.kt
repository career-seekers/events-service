package org.careerseekers.cseventsservice.services.interfaces

import org.springframework.data.jpa.repository.JpaRepository

interface BasicApiService<T, ID> {
    val repository: JpaRepository<T, ID>
}