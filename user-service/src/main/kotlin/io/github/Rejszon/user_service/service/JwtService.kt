package io.github.Rejszon.user_service.service

import io.github.Rejszon.user_service.entity.User
import io.github.Rejszon.user_service.repository.UserRepository
import io.jsonwebtoken.Jwts
import io.jsonwebtoken.security.Keys
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.security.core.userdetails.UsernameNotFoundException
import org.springframework.stereotype.Service
import java.util.Date
import javax.crypto.SecretKey

@Service
class JwtService(
    @Value("\${jwt.secret}") private val jwtSecret: String,
    @Value("\${jwt.expiration}") private val expiration: Long,
    private val userRepository: UserRepository,
) {
    private val key: SecretKey by lazy {
        Keys.hmacShaKeyFor(jwtSecret.toByteArray())
    }

    fun generateToken(user: User): String{
        return Jwts.builder()
            .issuer("user-service")
            .subject(user.id.toString())
            .claim("role",user.role)
            .claim("userId",user.id)
            .issuedAt(Date())
            .expiration(Date(Date().time + expiration))
            .signWith(key)
            .compact()
    }


}
