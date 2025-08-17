package com.joo.real_world.user.presentation

import com.joo.real_world.user.application.UserDto
import com.joo.real_world.user.presentation.response.UserResponse

fun UserDto.toUserResponse(token: String? = null): UserResponse {
    return UserResponse(
        id = this.id,
        username = this.username,
        email = this.email,
        token = token,
        bio = this.bio,
        image = this.image
    )
}