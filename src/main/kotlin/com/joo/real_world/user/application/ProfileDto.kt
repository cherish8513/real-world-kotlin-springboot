package com.joo.real_world.user.application

data class ProfileDto(
    val username: String,
    val bio: String?,
    val image: String?,
    val following: Boolean
)