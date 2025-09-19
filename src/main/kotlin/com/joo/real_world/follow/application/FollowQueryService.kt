package com.joo.real_world.follow.application

interface FollowQueryService {
    fun isFollowing(followerId: Long, followeeId: Long): Boolean
}