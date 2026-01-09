package org.careerseekers.cseventsservice.config

import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.graphql.client.HttpGraphQlClient
import org.springframework.http.HttpHeaders
import org.springframework.http.MediaType
import org.springframework.web.reactive.function.client.WebClient

@Configuration
class GraphQLClientConfig(private val graphQlClients: GraphQLClientsProperties) {

    @Bean
    @Qualifier("users-service-graphql")
    fun usersServiceGraphQlClient(): HttpGraphQlClient {
        val webClient = WebClient.builder()
            .baseUrl(graphQlClients.usersService)
            .defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
            .codecs { configurer ->
                configurer.defaultCodecs().maxInMemorySize(1024 * 1024)
            }
            .build()

        return HttpGraphQlClient.builder(webClient)
            .build()
    }
}