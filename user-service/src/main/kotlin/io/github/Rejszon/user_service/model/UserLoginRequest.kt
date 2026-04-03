package io.github.Rejszon.user_service.model

import jakarta.validation.constraints.Email
import jakarta.validation.constraints.NotBlank

data class UserLoginRequest(
    @field:NotBlank(message = "Email is required")
    @field:Email(message = "Email is invalid")
    val email: String,

    @field:NotBlank(message = "Password is required")
    val password: String
)
