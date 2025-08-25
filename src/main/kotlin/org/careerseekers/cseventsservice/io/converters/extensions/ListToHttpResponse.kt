package org.careerseekers.cseventsservice.io.converters.extensions

import org.careerseekers.cseventsservice.io.BasicSuccessfulResponse

fun <T> List<T>.toHttpResponse(): BasicSuccessfulResponse<List<T>> {
    return BasicSuccessfulResponse(this)
}