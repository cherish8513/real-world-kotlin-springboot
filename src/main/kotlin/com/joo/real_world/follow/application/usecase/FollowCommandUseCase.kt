package com.joo.real_world.follow.application.usecase

import com.joo.real_world.user.application.ProfileDto

interface FollowCommandUseCase {
    fun follow(followerId: Long, followeeUsername: String): ProfileDto
    fun unfollow(followerId: Long, followeeUsername: String): ProfileDto
}