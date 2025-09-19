package com.joo.real_world.user.application.usecase

import com.joo.real_world.user.application.ModifyUserDto
import com.joo.real_world.user.application.UserDto

interface UserCommandUseCase {
    fun register(username: String, email: String, password: String): UserDto
    fun modifyUser(modifyUserDto: ModifyUserDto): UserDto
}