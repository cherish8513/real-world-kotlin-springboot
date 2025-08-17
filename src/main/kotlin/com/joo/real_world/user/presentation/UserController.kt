package com.joo.real_world.user.presentation

import com.joo.real_world.common.config.ApiController
import com.joo.real_world.user.application.service.UserService
import jakarta.validation.Valid
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping

@ApiController
@RequestMapping("/user")
class UserController(
    private val userService: UserService
) {
    @PostMapping
    fun register(@Valid @RequestBody registerRequest: RegisterRequest) {
        userService.register(
            username = registerRequest.username,
            email = registerRequest.email,
            password = registerRequest.password
        )
    }
}