package org.careerseekers.cseventsservice.annotations

import org.careerseekers.cseventsservice.enums.UsersRoles

@Target(AnnotationTarget.FUNCTION, AnnotationTarget.CLASS)
@Retention(AnnotationRetention.RUNTIME)
annotation class AccessUntil(
    val until: String,
    val allowedRoles: Array<UsersRoles> = [],
    val errorMessage: String = "",
)
