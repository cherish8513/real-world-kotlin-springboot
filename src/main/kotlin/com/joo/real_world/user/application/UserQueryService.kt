package com.joo.real_world.user.application

interface UserQueryService {
    fun getUser(userId: Long): UserDto
    fun getUser(username: String): UserDto
}