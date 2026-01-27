package org.careerseekers.cseventsservice.io.converters.extensions.rpc

import com.careerseekers.grpc.users.UserId

fun Long.toUserId(): UserId = UserId.newBuilder().setId(this).build()