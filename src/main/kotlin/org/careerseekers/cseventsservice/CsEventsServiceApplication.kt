package org.careerseekers.cseventsservice

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.retry.annotation.EnableRetry

@EnableRetry
@SpringBootApplication
class CsEventsServiceApplication

fun main(args: Array<String>) {
    runApplication<CsEventsServiceApplication>(*args)
}
