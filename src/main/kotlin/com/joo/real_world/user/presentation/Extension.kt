package com.joo.real_world.user.presentation

import com.joo.real_world.user.application.ProfileDto
import com.joo.real_world.user.application.UserDto
import com.joo.real_world.user.presentation.response.Profile
import com.joo.real_world.user.presentation.response.ProfileResponse
import com.joo.real_world.user.presentation.response.User
import com.joo.real_world.user.presentation.response.UserResponse

fun UserDto.toUserResponse(token: String? = null): UserResponse {
    return UserResponse(
        user = User(
            id = this.id,
            username = this.username,
            email = this.email,
            token = token,
            bio = this.bio,
            image = this.image
        )
    )
}

fun ProfileDto.toProfileResponse(): ProfileResponse {
    return ProfileResponse(
        profile = Profile(
            username = this.username,
            bio = this.bio,
            image = this.image,
            following = this.following
        )
    )
}