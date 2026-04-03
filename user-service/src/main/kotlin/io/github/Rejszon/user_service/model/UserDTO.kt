package io.github.Rejszon.user_service.model

data class UserDTO(
    val id: Long,
    val email: String,
    val role: UserRole,
)
