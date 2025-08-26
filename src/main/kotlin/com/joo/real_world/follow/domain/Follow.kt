package com.joo.real_world.follow.domain

import com.joo.real_world.common.exception.CustomExceptionType
import com.joo.real_world.user.domain.vo.UserId

class Follow(
    val followerId: UserId,
    val followeeId: UserId
) {
    companion object {
        fun create(followerId: UserId, followeeId: UserId): Follow {
            require(followerId != followeeId) { throw CustomExceptionType.BAD_REQUEST.toException(("You can't follow yourself")) }
            return Follow(
                followerId = followerId,
                followeeId = followeeId
            )
        }
    }
}