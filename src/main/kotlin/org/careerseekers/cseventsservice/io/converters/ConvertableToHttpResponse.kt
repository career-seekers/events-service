@file:Suppress("UNCHECKED_CAST")

package org.careerseekers.cseventsservice.io.converters

import org.careerseekers.cseventsservice.io.BasicSuccessfulResponse

interface ConvertableToHttpResponse<T : ConvertableToHttpResponse<T>> {
    fun toHttpResponse(): BasicSuccessfulResponse<T> = BasicSuccessfulResponse(this as T)
}