package org.careerseekers.cseventsservice.clients

import org.careerseekers.cseventsservice.dto.graphql.GqlUser
import org.careerseekers.cseventsservice.dto.graphql.GqlUserEmailResponse
import org.careerseekers.cseventsservice.exceptions.BadRequestException
import org.careerseekers.cseventsservice.exceptions.NotFoundException
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.graphql.client.HttpGraphQlClient
import org.springframework.stereotype.Service

@Service
class GraphQlUsersServiceClient(
    @param:Qualifier("users-service-graphql")
    private val usersServiceGraphQlClient: HttpGraphQlClient,
) {

    fun getUserById(id: Long): GqlUser {
        return usersServiceGraphQlClient
            .documentName("getUser")
            .variable("id", id)
            .retrieve("user")
            .toEntity(GqlUser::class.java)
            .block() ?: throw NotFoundException("Пользователь с id $id не найден.")
    }

    fun usersByChildIds(ids: List<Long>): List<String> {
        return usersServiceGraphQlClient
            .documentName("getUsersEmailByChildIds")
            .variable("ids", ids)
            .retrieve("usersByChildIds")
            .toEntityList(GqlUserEmailResponse::class.java)
            .map { emails: List<GqlUserEmailResponse> -> emails.map { it.email } }
            .block()
            ?: throw BadRequestException("Что-то пошло не так в работе сервера. Проверьте работу метода usersByChildIds в GraphQlUsersServiceClient.")
    }
}