package com.joo.real_world.user.presentation

import com.joo.real_world.common.config.ApiController
import com.joo.real_world.security.AuthService
import com.joo.real_world.user.application.service.UserService
import com.joo.real_world.user.presentation.request.LoginRequest
import com.joo.real_world.user.presentation.request.RegisterRequest
import com.joo.real_world.user.presentation.response.UserResponse
import jakarta.validation.Valid
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping

@ApiController
@RequestMapping("/users")
class UserLoginController(
    private val userService: UserService,
    private val authService: AuthService
) {
    @PostMapping
    fun register(@Valid @RequestBody registerRequest: RegisterRequest): UserResponse {
        return userService.register(
            username = registerRequest.registerUser.username,
            email = registerRequest.registerUser.email,
            password = registerRequest.registerUser.password
        ).toUserResponse()
    }

    @PostMapping("/login")
    fun loginUser(@Valid @RequestBody loginRequest: LoginRequest): UserResponse {
        return userService.getUser(email = loginRequest.loginUser.email, password = loginRequest.loginUser.password)
            .let { it.toUserResponse(authService.login(it.id)) }
    }
}