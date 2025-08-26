package com.joo.real_world.follow.domain

import com.joo.real_world.common.exception.CustomExceptionType
import org.springframework.stereotype.Component

@Component
class FollowDomainService(
    private val followRepository: FollowRepository
) {
    fun validateCanFollow(follow: Follow) {
        if (follow.followerId == follow.followeeId) {
            throw CustomExceptionType.SELF_FOLLOW_NOT_ALLOWED.toException()
        }
        if (followRepository.isFollowing(follow.followerId, follow.followeeId)) {
            throw CustomExceptionType.ALREADY_FOLLOW.toException()
        }
    }

    fun validateCanUnfollow(follow: Follow) {
        if (!followRepository.isFollowing(follow.followerId, follow.followeeId)) {
            throw CustomExceptionType.NOT_FOLLOW.toException()
        }
    }
}