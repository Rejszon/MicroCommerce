package io.github.Rejszon.user_service.model

data class UserRoleChangeRequest(
    val id: Long,
    val role: UserRole,
)
