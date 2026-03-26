package io.github.Rejszon.user_service.service

import io.github.Rejszon.user_service.entity.User
import io.github.Rejszon.user_service.model.UserRegistrationRequest
import io.github.Rejszon.user_service.model.UserRole
import io.github.Rejszon.user_service.repository.UserRepository
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Service

@Service
class UserService(
    private val repo: UserRepository,
    private val passwordEncoder: PasswordEncoder
) {
    fun registerUser(request: UserRegistrationRequest,) {
        if (repo.existsByEmail(request.email)) {
            throw IllegalArgumentException("User email already exists")
        }
        val hashedPassword: String = passwordEncoder.encode(request.password) ?: throw IllegalStateException("Password encoder cannot be null or blank")
        repo.save(User( email = request.email, password = hashedPassword, role = UserRole.BUYER))
    }
}