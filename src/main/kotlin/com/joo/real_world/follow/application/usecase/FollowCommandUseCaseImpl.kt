package com.joo.real_world.follow.application.usecase

import com.joo.real_world.follow.domain.Follow
import com.joo.real_world.follow.domain.FollowDomainService
import com.joo.real_world.follow.domain.FollowRepository
import com.joo.real_world.user.application.ProfileDto
import com.joo.real_world.user.application.UserQueryService
import com.joo.real_world.user.domain.vo.UserId
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Transactional(rollbackFor = [Exception::class])
@Service
class FollowCommandUseCaseImpl(
    private val followRepository: FollowRepository,
    private val followDomainService: FollowDomainService,
    private val userQueryService: UserQueryService,
) : FollowCommandUseCase {
    override fun follow(followerId: Long, followeeUsername: String): ProfileDto {
        val follower = userQueryService.getUser(followerId)
        val followee = userQueryService.getUser(followeeUsername)
        val follow = Follow.create(followerId = UserId(follower.id), followeeId = UserId(followee.id))

        followDomainService.validateCanFollow(follow)
        followRepository.follow(follow)

        return ProfileDto(
            username = followeeUsername,
            bio = followee.bio,
            image = followee.image,
            following = true
        )
    }

    override fun unfollow(followerId: Long, followeeUsername: String): ProfileDto {
        val follower = userQueryService.getUser(followerId)
        val followee = userQueryService.getUser(followeeUsername)
        val follow = Follow.create(followerId = UserId(follower.id), followeeId = UserId(followee.id))
        followDomainService.validateCanUnfollow(follow)
        followRepository.unFollow(followerId = follow.followerId, followeeId = follow.followeeId)

        return ProfileDto(
            username = followeeUsername,
            bio = followee.bio,
            image = followee.image,
            following = false
        )
    }
}