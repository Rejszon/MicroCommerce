package io.github.Rejszon.user_service.controller

import io.github.Rejszon.user_service.model.UserDTO
import io.github.Rejszon.user_service.service.UserService
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/users")
class UserController(
    private val service: UserService
) {
    @GetMapping("/{id}")
    fun getUser(@PathVariable id: Long): ResponseEntity<Any> {
        try {
            val response = service.getUserById(id)
            return ResponseEntity.ok(response)
        }
        catch (e: IllegalArgumentException) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.message)
        }
    }

}
