package com.joo.real_world.user.application

import com.joo.real_world.user.domain.FollowRepository
import com.joo.real_world.user.domain.vo.UserId
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Transactional(readOnly = true, rollbackFor = [Exception::class])
@Service
class FollowRelationServiceImpl(
    private val followRepository: FollowRepository
): FollowRelationService {
    override fun isFollowing(followerId: Long, followeeId: Long): Boolean {
        return followRepository.isFollowing(followerId = UserId(followerId), followeeId = UserId(followeeId))
    }
}