package org.careerseekers.cseventsservice.utils

import io.jsonwebtoken.Claims
import io.jsonwebtoken.ExpiredJwtException
import io.jsonwebtoken.Jwts
import io.jsonwebtoken.security.Keys
import org.careerseekers.cseventsservice.cache.UsersCacheClient
import org.careerseekers.cseventsservice.dto.UsersCacheDto
import org.careerseekers.cseventsservice.exceptions.JwtAuthenticationException
import org.careerseekers.cseventsservice.exceptions.NotFoundException
import org.springframework.beans.factory.annotation.Value
import java.util.Date
import kotlin.text.toByteArray

@Utility
class JwtUtil(private val usersCacheClient: UsersCacheClient) {
    @Value("\${config.jwt.secret}")
    private lateinit var jwtSecret: String

    fun verifyToken(token: String, throwTimeLimit: Boolean = true): Boolean {
        val claims = getClaims(token) ?: throw JwtAuthenticationException("Невалидное содержание токена.")
        if (!claims.expiration.after(Date()) && throwTimeLimit) {
            throw JwtAuthenticationException("Срок жизни токена истёк.")
        }

        return true
    }

    fun getUserFromToken(token: String): UsersCacheDto? {
        val claims = getClaims(token)

        return usersCacheClient.getItemFromCache((claims?.get("id") as Int).toLong())
            ?: throw NotFoundException("Пользователь с ID ${claims["id"]} не найден.")
    }

    fun getClaims(token: String): Claims? {
        val claims = try {
            Jwts.parser()
                .verifyWith(Keys.hmacShaKeyFor(jwtSecret.toByteArray()))
                .build()
                .parseSignedClaims(token)
                .payload
        } catch (_: ExpiredJwtException) {
            throw JwtAuthenticationException("Срок жизни токена истёк.")
        }

        return claims
    }

}