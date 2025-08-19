package com.joo.real_world.user.application.service

import com.joo.real_world.user.application.ModifyUserDto
import com.joo.real_world.user.application.UserDto

interface UserService {
    fun register(username: String, email: String, password: String): UserDto
    fun getUser(email: String, password: String): UserDto
    fun getUser(userId: Long): UserDto
    fun modifyUser(modifyUserDto: ModifyUserDto): UserDto
}