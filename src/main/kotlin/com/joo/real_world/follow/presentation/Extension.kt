package com.joo.real_world.follow.presentation

import com.joo.real_world.user.application.ProfileDto
import com.joo.real_world.user.presentation.response.Profile
import com.joo.real_world.user.presentation.response.ProfileResponse

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