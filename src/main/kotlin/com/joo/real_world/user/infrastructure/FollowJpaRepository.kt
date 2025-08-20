package com.joo.real_world.user.infrastructure

import com.joo.real_world.user.domain.Follow
import com.joo.real_world.user.domain.FollowRepository
import com.joo.real_world.user.domain.vo.UserId
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

interface IFollowerJpaRepository : JpaRepository<FollowEntity, FollowerId>

@Repository
class FollowJpaRepository(
    private val followerJpaRepository: IFollowerJpaRepository
) : FollowRepository {
    override fun follow(follow: Follow): Follow {
        return followerJpaRepository.save(follow.toEntity()).toDomain()
    }

    override fun isFollowing(followerId: UserId, followeeId: UserId): Boolean {
        return followerJpaRepository.existsById(
            FollowerId(
                followerId = followerId.value,
                followeeId = followeeId.value
            )
        )
    }

    override fun unFollow(followerId: UserId, followeeId: UserId) {
        followerJpaRepository.deleteById(FollowerId(followerId = followerId.value, followeeId = followeeId.value))
    }
}