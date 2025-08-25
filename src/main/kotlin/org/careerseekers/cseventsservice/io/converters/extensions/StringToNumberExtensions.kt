package org.careerseekers.cseventsservice.io.converters.extensions

import org.careerseekers.cseventsservice.exceptions.InvalidNumberFormatException

fun String.toLongOrThrow(message: String = "Invalid long value '$this'"): Long =
    this.toLongOrNull() ?: throw InvalidNumberFormatException(message)

fun String.toShortOrThrow(message: String = "Invalid short value '$this'"): Short =
    this.toShortOrNull() ?: throw InvalidNumberFormatException(message)
