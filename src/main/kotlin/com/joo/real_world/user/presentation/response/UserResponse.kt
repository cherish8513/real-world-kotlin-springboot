package com.joo.real_world.user.presentation.response

data class UserResponse(
    val id: Long,
    val email: String,
    val username: String,
    val bio: String?,
    val image: String?,
    val token: String?,
)