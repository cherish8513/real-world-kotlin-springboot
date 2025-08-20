package com.joo.real_world.user.presentation.response

data class ProfileResponse(
    val profile: Profile
)

data class Profile(
    val username: String,
    val bio: String?,
    val image: String?,
    val following: Boolean
)