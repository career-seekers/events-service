package org.careerseekers.cseventsservice.io.converters.extensions

import org.careerseekers.cseventsservice.io.BasicSuccessfulResponse
import org.springframework.data.domain.Page

fun <T> Page<T>.toHttpResponse() = BasicSuccessfulResponse<Page<T>>(this)