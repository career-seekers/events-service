package org.careerseekers.cseventsservice.exceptions

import org.springframework.http.HttpStatus

class GrpcServiceUnavailableException(
    message: String,
) : AbstractHttpException(HttpStatus.SERVICE_UNAVAILABLE.value(), message)