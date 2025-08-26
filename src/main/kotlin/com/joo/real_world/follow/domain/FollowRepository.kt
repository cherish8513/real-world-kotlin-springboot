package com.joo.real_world.follow.domain

import com.joo.real_world.user.domain.vo.UserId

interface FollowRepository {
    fun follow(follow: Follow): Follow
    fun isFollowing(followerId: UserId, followeeId: UserId): Boolean
    fun unFollow(followerId: UserId, followeeId: UserId)
}