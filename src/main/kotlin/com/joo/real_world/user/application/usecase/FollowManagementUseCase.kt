package com.joo.real_world.user.application.usecase

import com.joo.real_world.user.application.ProfileDto

interface FollowManagementUseCase {
    fun follow(followerId: Long, followeeUsername: String): ProfileDto
    fun unfollow(followerId: Long, followeeUsername: String): ProfileDto
}