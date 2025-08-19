package com.joo.real_world.user.application.usecase

import com.joo.real_world.user.application.UserDto

interface RegisterUseCase {
    fun register(username: String, email: String, password: String): UserDto
}