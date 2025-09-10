package com.joo.real_world.follow.infrastructure

import com.joo.real_world.follow.domain.Follow
import com.joo.real_world.follow.domain.FollowRepository
import com.joo.real_world.user.domain.vo.UserId
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

interface IFollowerJpaRepository : JpaRepository<FollowEntity, FollowId>

@Repository
class FollowJpaRepository(
    private val followerJpaRepository: IFollowerJpaRepository
) : FollowRepository {
    override fun follow(follow: Follow): Follow {
        return followerJpaRepository.save(follow.toEntity()).toDomain()
    }

    override fun isFollowing(followerId: UserId, followeeId: UserId): Boolean {
        return followerJpaRepository.existsById(
            FollowId(
                followerId = followerId.value,
                followeeId = followeeId.value
            )
        )
    }

    override fun unFollow(followerId: UserId, followeeId: UserId) {
        followerJpaRepository.deleteById(FollowId(followerId = followerId.value, followeeId = followeeId.value))
    }
}