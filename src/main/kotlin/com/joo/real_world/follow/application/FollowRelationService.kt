package com.joo.real_world.follow.application

interface FollowRelationService {
    fun isFollowing(followerId: Long, followeeId: Long): Boolean
}