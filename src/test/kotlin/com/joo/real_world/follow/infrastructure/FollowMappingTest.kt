package com.joo.real_world.follow.infrastructure

import com.joo.real_world.follow.domain.Follow
import com.joo.real_world.user.domain.vo.UserId
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

class FollowMappingTest {

    @Test
    fun `FollowEntity toDomain mapping`() {
        // given
        val entity = FollowEntity(FollowId(followerId = 1L, followeeId = 2L))

        // when
        val follow = entity.toDomain()

        // then
        assertThat(follow.followerId.value).isEqualTo(entity.followerId)
        assertThat(follow.followeeId.value).isEqualTo(entity.followeeId)
    }

    @Test
    fun `Follow toEntity mapping`() {
        // given
        val follow = Follow(
            followerId = UserId(3L),
            followeeId = UserId(4L)
        )

        // when
        val entity = follow.toEntity()

        // then
        assertThat(entity.followerId).isEqualTo(follow.followerId.value)
        assertThat(entity.followeeId).isEqualTo(follow.followeeId.value)
    }
}
