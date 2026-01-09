package org.careerseekers.cseventsservice.config

import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.context.annotation.Configuration

@Configuration
@ConfigurationProperties("graphql.client")
class GraphQLClientsProperties {
    lateinit var usersService: String
}
