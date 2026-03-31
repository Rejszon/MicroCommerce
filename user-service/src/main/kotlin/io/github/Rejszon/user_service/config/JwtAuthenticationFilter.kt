package io.github.Rejszon.user_service.config

import io.github.Rejszon.user_service.service.JwtService
import jakarta.servlet.FilterChain
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource
import org.springframework.stereotype.Component
import org.springframework.web.filter.OncePerRequestFilter

@Component
class JwtAuthenticationFilter(
    private val jwtService: JwtService,
    private val userDetailsService: UserDetailsService
): OncePerRequestFilter() {
    override fun doFilterInternal(
        req: HttpServletRequest,
        res: HttpServletResponse,
        chain: FilterChain
    ){
        val authHeader = req.getHeader("Authorization")
        if(authHeader == null || !authHeader.startsWith("Bearer ")) {
            chain.doFilter(req, res)
            return
        }
        println("here")
        val jwt = authHeader.substring("Bearer ".length)
        val emailFromToken = jwtService.validateToken(jwt)
        if(emailFromToken != null ){
            val user = userDetailsService.loadUserByUsername(emailFromToken)
            val authentication = UsernamePasswordAuthenticationToken(user,null, user.authorities)

            authentication.details = WebAuthenticationDetailsSource().buildDetails(req)
            SecurityContextHolder.getContext().authentication = authentication
        }
        chain.doFilter(req, res)
        return
    }
}
