package org.careerseekers.cseventsservice.io

interface AbstractResponse<T> {
    val status: Int
    val message: T?
}