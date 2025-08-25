package org.careerseekers.cseventsservice.exceptions

import javax.naming.AuthenticationException

class JwtAuthenticationException(message: String) : AuthenticationException(message)