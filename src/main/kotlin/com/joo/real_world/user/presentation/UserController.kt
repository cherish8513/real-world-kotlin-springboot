package com.joo.real_world.user.presentation

import com.joo.real_world.common.config.ApiController
import com.joo.real_world.security.UserSession
import com.joo.real_world.user.application.ModifyUserDto
import com.joo.real_world.user.application.UserDto
import com.joo.real_world.user.application.service.UserService
import com.joo.real_world.user.presentation.request.ModifyUserRequest
import com.joo.real_world.user.presentation.response.UserResponse
import jakarta.validation.Valid
import org.springframework.security.core.annotation.AuthenticationPrincipal
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping

@ApiController
@RequestMapping("/user")
class UserController(
    private val userService: UserService
) {
    @GetMapping
    fun getCurrentUser(@AuthenticationPrincipal userSession: UserSession): UserResponse {
        return userService.getUser(userSession.userId).toUserResponse()
    }

    @PutMapping
    fun modifyUser(
        @Valid @RequestBody modifyUserRequest: ModifyUserRequest,
        @AuthenticationPrincipal userSession: UserSession
    ): UserResponse {
        return userService.modifyUser(
            ModifyUserDto(
                id = userSession.userId,
                username = modifyUserRequest.modifyUser.username,
                email = modifyUserRequest.modifyUser.email,
                password = modifyUserRequest.modifyUser.password,
                bio = modifyUserRequest.modifyUser.bio,
                image = modifyUserRequest.modifyUser.image,
            )
        ).toUserResponse()
    }
}