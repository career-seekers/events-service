package org.careerseekers.cseventsservice.io.converters.extensions

import org.careerseekers.cseventsservice.io.BasicSuccessfulResponse

fun String.toHttpResponse(): BasicSuccessfulResponse<String> {
    return BasicSuccessfulResponse(this)
}