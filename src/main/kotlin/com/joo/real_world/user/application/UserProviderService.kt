package com.joo.real_world.user.application

interface UserProviderService {
    fun getUser(userId: Long): UserDto
}