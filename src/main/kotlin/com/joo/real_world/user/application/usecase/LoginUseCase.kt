package com.joo.real_world.user.application.usecase

import com.joo.real_world.user.application.UserDto

interface LoginUseCase {
    fun getUser(email: String, password: String): UserDto
}