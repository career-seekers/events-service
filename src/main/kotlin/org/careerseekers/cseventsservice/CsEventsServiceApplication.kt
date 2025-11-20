package org.careerseekers.cseventsservice

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.retry.annotation.EnableRetry
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker

@EnableRetry
@EnableWebSocketMessageBroker
@SpringBootApplication
class CsEventsServiceApplication

fun main(args: Array<String>) {
    runApplication<CsEventsServiceApplication>(*args)
}
