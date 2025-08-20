package com.joo.real_world.user.application.usecase

import com.joo.real_world.common.exception.CustomExceptionType
import com.joo.real_world.common.util.assertNotNull
import com.joo.real_world.user.application.ProfileDto
import com.joo.real_world.user.domain.Follow
import com.joo.real_world.user.domain.FollowRepository
import com.joo.real_world.user.domain.UserRepository
import com.joo.real_world.user.domain.vo.UserId
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Transactional(rollbackFor = [Exception::class])
@Service
class FollowManagementUseCaseImpl(
    private val followRepository: FollowRepository,
    private val userRepository: UserRepository
) : FollowManagementUseCase {
    override fun follow(followerId: Long, followeeUsername: String): ProfileDto {
        val follower = userRepository.findByUserId(UserId(followerId)).assertNotNull()
        val followee =
            userRepository.findByUsername(followeeUsername) ?: throw CustomExceptionType.INVALID_USER.toException()

        validateFollowAvailable(followerId = follower.id.assertNotNull(), followeeId = followee.id.assertNotNull())

        followRepository.follow(
            Follow.create(
                followerId = follower.id.assertNotNull(),
                followeeId = followee.id.assertNotNull()
            )
        )

        return ProfileDto(
            username = followeeUsername,
            bio = followee.bio,
            image = followee.image,
            following = true
        )
    }

    override fun unfollow(followerId: Long, followeeUsername: String): ProfileDto {
        val follower = userRepository.findByUserId(UserId(followerId)).assertNotNull()
        val followee =
            userRepository.findByUsername(followeeUsername) ?: throw CustomExceptionType.INVALID_USER.toException()
        validateUnfollowAvailable(followerId = follower.id.assertNotNull(), followeeId = followee.id.assertNotNull())
        followRepository.unFollow(followerId = follower.id.assertNotNull(), followeeId = followee.id.assertNotNull())

        return ProfileDto(
            username = followeeUsername,
            bio = followee.bio,
            image = followee.image,
            following = false
        )
    }

    private fun validateFollowAvailable(followerId: UserId, followeeId: UserId) {
        if (isFollowing(followerId = followerId, followeeId = followeeId)) {
            throw CustomExceptionType.ALREADY_FOLLOW.toException()
        }
    }

    private fun validateUnfollowAvailable(followerId: UserId, followeeId: UserId) {
        if (!isFollowing(followerId = followerId, followeeId = followeeId)) {
            throw CustomExceptionType.NOT_FOLLOW.toException()
        }
    }

    private fun isFollowing(followerId: UserId, followeeId: UserId): Boolean {
        if (followerId == followeeId) throw CustomExceptionType.UNEXPECTED_ERROR_OCCURRED.toException()
        return followRepository.isFollowing(followerId, followeeId)
    }
}