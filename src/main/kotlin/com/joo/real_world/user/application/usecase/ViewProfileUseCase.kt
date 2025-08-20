package com.joo.real_world.user.application.usecase

import com.joo.real_world.user.application.ProfileDto

interface ViewProfileUseCase {
    fun getProfile(username: String, viewerId: Long?): ProfileDto
}