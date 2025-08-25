package org.careerseekers.cseventsservice.exceptions

abstract class AbstractHttpException(val status: Int, override val message: String?) : RuntimeException(message)