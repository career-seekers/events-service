package org.careerseekers.cseventsservice.dto.graphql

data class GqlUser(
    val id: Long,
    val firstName: String,
    val lastName: String,
    val patronymic: String,
    val email: String,
) {
    fun getFullName() = "$lastName $firstName $patronymic"
}