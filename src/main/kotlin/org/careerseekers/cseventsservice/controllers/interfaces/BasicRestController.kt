package org.careerseekers.cseventsservice.controllers.interfaces

import org.careerseekers.cseventsservice.services.interfaces.BasicApiService

interface BasicRestController {
    val service: BasicApiService<*, *>
}