package com.joo.real_world.user.domain

import com.joo.real_world.user.domain.vo.UserId

interface FollowerRepository {
    fun existsFollowing(followerId: UserId, followeeId: UserId): Boolean
}