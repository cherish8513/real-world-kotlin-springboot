package com.joo.real_world.user.application.usecase

import com.joo.real_world.user.application.ModifyUserDto
import com.joo.real_world.user.application.UserDto

interface UpdateUserUseCase {
    fun modifyUser(modifyUserDto: ModifyUserDto): UserDto
}