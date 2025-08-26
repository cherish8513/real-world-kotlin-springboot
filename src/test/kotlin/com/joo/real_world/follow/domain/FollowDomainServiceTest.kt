package com.joo.real_world.follow.domain

import com.joo.real_world.common.exception.CustomExceptionType
import com.joo.real_world.user.domain.vo.UserId
import io.mockk.every
import io.mockk.mockk
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import kotlin.test.assertFailsWith

class FollowDomainServiceTest {

    private val followRepository: FollowRepository = mockk()
    private val followDomainService = FollowDomainService(followRepository)

    private val followerId = UserId(1L)
    private val followeeId = UserId(2L)

    private val follow = Follow(
        followerId = followerId,
        followeeId = followeeId
    )

    @Test
    fun `self follow - throw SELF_FOLLOW_NOT_ALLOWED`() {
        // given
        val selfFollow = Follow(followerId = followerId, followeeId = followerId)

        // when & then
        val exception = assertFailsWith<RuntimeException> {
            followDomainService.validateCanFollow(selfFollow)
        }
        assertEquals(CustomExceptionType.SELF_FOLLOW_NOT_ALLOWED.toException()::class, exception::class)
    }

    @Test
    fun `already following - throw ALREADY_FOLLOW`() {
        // given
        every { followRepository.isFollowing(followerId, followeeId) } returns true

        // when & then
        val exception = assertThrows<RuntimeException> {
            followDomainService.validateCanFollow(follow)
        }
        assertEquals(CustomExceptionType.ALREADY_FOLLOW.toException()::class, exception::class)
    }

    @Test
    fun `success to validate follow`() {
        // given
        every { followRepository.isFollowing(followerId, followeeId) } returns false

        // when & then
        followDomainService.validateCanFollow(follow)
    }

    @Test
    fun `not following - throw NOT_FOLLOW`() {
        // given
        every { followRepository.isFollowing(followerId, followeeId) } returns false

        // when & then
        val exception = assertFailsWith<RuntimeException> {
            followDomainService.validateCanUnfollow(follow)
        }
        assertEquals(CustomExceptionType.NOT_FOLLOW.toException()::class, exception::class)
    }

    @Test
    fun `success to validate unfollow`() {
        // given
        every { followRepository.isFollowing(followerId, followeeId) } returns true

        // when & then
        followDomainService.validateCanUnfollow(follow)
    }
}