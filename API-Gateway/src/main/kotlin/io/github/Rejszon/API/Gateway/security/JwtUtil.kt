package io.github.Rejszon.API.Gateway.security

import io.jsonwebtoken.Claims
import io.jsonwebtoken.Jwts
import io.jsonwebtoken.security.Keys
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Component

@Component
class JwtUtil(
    @Value("\${jwt.secret}") private val jwtSecret: String,
) {
    private fun getSigningKey() = Keys.hmacShaKeyFor(jwtSecret.toByteArray())

    fun validateToken(token: String) {
        Jwts.parser()
            .verifyWith(getSigningKey())
            .build()
            .parseSignedClaims(token)
    }

    private fun getClaims(token: String): Claims {
        return Jwts.parser()
            .verifyWith(getSigningKey())
            .build()
            .parseSignedClaims(token)
            .payload
    }

    fun getUserId(token: String): String {
        return getClaims(token).subject
    }

    fun getUserRole(token: String): String {
        return getClaims(token)["role"].toString()
    }
}
