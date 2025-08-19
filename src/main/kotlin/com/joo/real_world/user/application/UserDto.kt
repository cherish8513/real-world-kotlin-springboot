package com.joo.real_world.user.application

data class UserDto(
    val id: Long,
    val username: String,
    val email: String,
    val bio: String?,
    val image: String?,
)

data class ModifyUserDto(
    val id: Long,
    val username: String?,
    val email: String?,
    val password: String?,
    val bio: String?,
    val image: String?,
)