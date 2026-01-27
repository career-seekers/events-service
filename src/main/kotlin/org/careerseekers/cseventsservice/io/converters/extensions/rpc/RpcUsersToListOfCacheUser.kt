package org.careerseekers.cseventsservice.io.converters.extensions.rpc

import com.careerseekers.grpc.users.Users
import org.careerseekers.cseventsservice.dto.UsersCacheDto

fun Users.toList(): List<UsersCacheDto> = this.usersList.map { it.toCache() }