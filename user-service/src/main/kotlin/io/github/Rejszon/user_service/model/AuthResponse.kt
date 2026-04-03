package io.github.Rejszon.user_service.model

data class AuthResponse(
    val accessToken: String,
    // TODO: val refreshToken: String,
    val type: String = "Bearer",

)
