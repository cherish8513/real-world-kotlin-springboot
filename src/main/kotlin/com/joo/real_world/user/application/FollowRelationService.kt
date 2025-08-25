package com.joo.real_world.user.application

interface FollowRelationService {
    fun isFollowing(followerId: Long, followeeId: Long): Boolean
}