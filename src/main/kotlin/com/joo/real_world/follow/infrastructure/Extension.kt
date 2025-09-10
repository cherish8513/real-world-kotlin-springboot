package com.joo.real_world.follow.infrastructure

import com.joo.real_world.follow.domain.Follow
import com.joo.real_world.user.domain.vo.UserId

fun FollowEntity.toDomain(): Follow =
    Follow(
        followerId = UserId(this.followerId),
        followeeId = UserId(this.followeeId)
    )

fun Follow.toEntity(): FollowEntity =
    FollowEntity(
        FollowId(
            followerId = this.followerId.value,
            followeeId = this.followeeId.value
        )
    )