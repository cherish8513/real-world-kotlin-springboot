package com.joo.real_world.user.domain

import com.joo.real_world.user.domain.vo.UserId

class Follow(
    val followerId: UserId,
    val followeeId: UserId
) {
    companion object {
        fun create(followerId: UserId, followeeId: UserId): Follow {
            require(followerId != followeeId) { "자기 자신을 팔로우할 수 없습니다" }
            return Follow(
                followerId = followerId,
                followeeId = followeeId
            )
        }
    }
}