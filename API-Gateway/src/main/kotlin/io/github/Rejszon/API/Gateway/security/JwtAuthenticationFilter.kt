package io.github.Rejszon.API.Gateway.security

import org.springframework.http.HttpHeaders
import org.springframework.cloud.gateway.filter.GatewayFilter
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory
import org.springframework.http.HttpStatus
import org.springframework.stereotype.Component
import org.springframework.web.server.ServerWebExchange
import reactor.core.publisher.Mono

@Component
class JwtAuthenticationFilter(
    private val jwtUtil: JwtUtil,
) : AbstractGatewayFilterFactory<JwtAuthenticationFilter.Config>(Config::class.java) {
    class Config

    override fun apply(config: Config): GatewayFilter {
        return GatewayFilter {exchange, chain ->
            val request = exchange.request

            val authHeader = request.headers.getFirst(HttpHeaders.AUTHORIZATION)
            if (authHeader.isNullOrBlank() || !authHeader.startsWith("Bearer ")){
                return@GatewayFilter rejectRequest(exchange)
            }
            val token = authHeader.substringAfter("Bearer ")
            try{
                jwtUtil.validateToken(token)

                val userId = jwtUtil.getUserId(token)
                val userRole = jwtUtil.getUserRole(token)

                val mutatedRequest = request.mutate()
                    .headers{
                        it.remove(HttpHeaders.AUTHORIZATION)

                        it.remove("X-User-Id")
                        it.remove("X-User-Role")
                    }
                    .header("X-User-Id", userId)
                    .header("X-User-Role", userRole )
                    .build()

                val mutatedExchange = exchange.mutate().request(mutatedRequest).build()
                return@GatewayFilter chain.filter(mutatedExchange)
            }
            catch (_: Exception){
                return@GatewayFilter rejectRequest(exchange)
            }
        }
    }

    private fun rejectRequest(exchange: ServerWebExchange): Mono<Void> {
        val response = exchange.response
        response.statusCode = HttpStatus.UNAUTHORIZED
        return response.setComplete()
    }
}

