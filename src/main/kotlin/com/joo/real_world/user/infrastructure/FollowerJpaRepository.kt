package com.joo.real_world.user.infrastructure

import com.joo.real_world.user.domain.FollowerRepository
import com.joo.real_world.user.domain.vo.UserId
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

interface IFollowerJpaRepository : JpaRepository<FollowerEntity, FollowerId>

@Repository
class FollowerJpaRepository(
    private val followerJpaRepository: IFollowerJpaRepository
) : FollowerRepository {
    override fun existsFollowing(followerId: UserId, followeeId: UserId): Boolean {
        return followerJpaRepository.existsById(
            FollowerId(
                followerId = followerId.value,
                followeeId = followeeId.value
            )
        )
    }
}