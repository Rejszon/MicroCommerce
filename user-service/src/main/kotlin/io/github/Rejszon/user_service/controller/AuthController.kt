package io.github.Rejszon.user_service.controller

import io.github.Rejszon.user_service.model.AuthResponse
import io.github.Rejszon.user_service.model.UserLoginRequest
import io.github.Rejszon.user_service.model.UserRegistrationRequest
import io.github.Rejszon.user_service.service.UserService
import jakarta.validation.Valid
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/auth")
class AuthController(private val service: UserService) {

    @PostMapping("/register")
    fun registerUser(@Valid @RequestBody request: UserRegistrationRequest): ResponseEntity<String> {
        return try {
            service.registerUser(request)
            ResponseEntity("User registered successfully", HttpStatus.CREATED)
        }
        catch (e: IllegalArgumentException) {
            ResponseEntity(e.message, HttpStatus.BAD_REQUEST)
        } catch (_: Exception) {
            ResponseEntity("Internal Server Error", HttpStatus.INTERNAL_SERVER_ERROR)
        }
    }

    @PostMapping("/login")
    fun loginUser(@Valid @RequestBody request: UserLoginRequest): ResponseEntity<Any> {
        return try {
            val token = service.loginUser(request)
            ResponseEntity.ok(AuthResponse(token))
        } catch (e: IllegalArgumentException) {
            ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(e.message)
        } catch (_: Exception) {
            ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Internal server error")
        }
    }
}
