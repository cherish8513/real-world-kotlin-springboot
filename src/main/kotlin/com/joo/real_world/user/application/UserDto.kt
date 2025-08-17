package com.joo.real_world.user.application

data class UserDto(
    val id: Long,
    val username: String,
    val email: String,
    val bio: String? = null,
    val image: String? = null,
)