package io.github.Rejszon.user_service.service

import io.github.Rejszon.user_service.entity.User
import io.github.Rejszon.user_service.entity.toDTO
import io.github.Rejszon.user_service.model.UserDTO
import io.github.Rejszon.user_service.model.UserLoginRequest
import io.github.Rejszon.user_service.model.UserRegistrationRequest
import io.github.Rejszon.user_service.model.UserRole
import io.github.Rejszon.user_service.repository.UserRepository
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Service

@Service
class UserService(
    private val repo: UserRepository,
    private val passwordEncoder: PasswordEncoder,
    private val jwtService: JwtService,
) {
    fun registerUser(request: UserRegistrationRequest) {
        if (repo.existsByEmail(request.email)) {
            throw IllegalArgumentException("User email already exists")
        }
        val hashedPassword: String = passwordEncoder.encode(request.password) ?: throw IllegalStateException("Password encoder cannot be null or blank")
        repo.save(User( email = request.email, password = hashedPassword, role = UserRole.BUYER))
    }
    fun loginUser(request: UserLoginRequest): String {
        val user = repo.findByEmail(request.email) ?: throw IllegalArgumentException("Credentials don't match")
        if (!passwordEncoder.matches(request.password, user.password)) {
            throw IllegalArgumentException("Credentials don't match")
        }
       return jwtService.generateToken(user)
    }
    fun getUserById(id: Long): UserDTO {
        val user = repo.findById(id).orElseThrow { IllegalArgumentException("User does not exist")}
        return user.toDTO()
    }
}
