package com.joo.real_world.user.application.usecase

import com.joo.real_world.user.application.ProfileDto

interface UserQueryUseCase {
    fun getProfile(username: String, viewerId: Long?): ProfileDto
}