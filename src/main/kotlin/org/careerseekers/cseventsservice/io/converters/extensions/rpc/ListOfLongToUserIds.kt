package org.careerseekers.cseventsservice.io.converters.extensions.rpc

import com.careerseekers.grpc.users.UserIds

fun List<Long>.toUserIds(): UserIds {
    return UserIds.newBuilder()
        .addAllIds(this.map { it.toUserId() })
        .build()
}
