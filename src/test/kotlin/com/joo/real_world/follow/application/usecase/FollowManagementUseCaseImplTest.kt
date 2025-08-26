package com.joo.real_world.follow.application.usecase

import com.joo.real_world.follow.domain.FollowDomainService
import com.joo.real_world.follow.domain.FollowRepository
import com.joo.real_world.user.application.UserDto
import com.joo.real_world.user.application.UserProviderService
import com.joo.real_world.user.domain.vo.UserId
import io.mockk.*
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test

class FollowManagementUseCaseImplTest {

    private val followRepository: FollowRepository = mockk()
    private val userProviderService: UserProviderService = mockk()
    private val followDomainService: FollowDomainService = mockk()

    private val useCase = FollowManagementUseCaseImpl(
        followRepository = followRepository,
        userProviderService = userProviderService,
        followDomainService = followDomainService
    )

    private val followerId = 1L
    private val followerUserId = UserId(followerId)
    private val followeeUsername = "testuser"
    private val followeeId = 2L
    private val followeeUserId = UserId(followeeId)

    private val followerUser = UserDto(
        id = followerUserId.value,
        username = "follower",
        email = "follower@test.com",
        bio = "follower bio",
        image = "follower.jpg"
    )

    private val followeeUser = UserDto(
        id = followeeUserId.value,
        username = followeeUsername,
        email = "followee@test.com",
        bio = "followee bio",
        image = "followee.jpg"
    )

    @BeforeEach
    fun setUp() {
        clearAllMocks()
    }

    @Nested
    @DisplayName("팔로우 기능")
    inner class FollowTest {

        @Test
        @DisplayName("정상적으로 팔로우를 할 수 있다")
        fun `should follow user successfully`() {
            // given
            every { userProviderService.getUser(followerUserId.value) } returns followerUser
            every { userProviderService.getUser(followeeUsername) } returns followeeUser
            every { followRepository.follow(any()) } returns mockk()
            every { followDomainService.validateCanFollow(any()) } just runs

            // when
            val result = useCase.follow(followerId, followeeUsername)

            // then
            assertEquals(followeeUsername, result.username)
            assertEquals(followeeUser.bio, result.bio)
            assertEquals(followeeUser.image, result.image)
            assertTrue(result.following)

            verify(exactly = 1) { userProviderService.getUser(followerUserId.value) }
            verify(exactly = 1) { userProviderService.getUser(followeeUsername) }
            verify(exactly = 1) { followRepository.follow(any()) }
        }
    }

    @Nested
    @DisplayName("언팔로우 기능")
    inner class UnfollowTest {

        @Test
        @DisplayName("정상적으로 언팔로우를 할 수 있다")
        fun `should unfollow user successfully`() {
            // given
            every { userProviderService.getUser(followerUserId.value) } returns followerUser
            every { userProviderService.getUser(followeeUsername) } returns followeeUser
            every { followRepository.unFollow(followerUserId, followeeUserId) } just Runs
            every { followDomainService.validateCanUnfollow(any()) } just runs

            // when
            val result = useCase.unfollow(followerId, followeeUsername)

            // then
            assertEquals(followeeUsername, result.username)
            assertEquals(followeeUser.bio, result.bio)
            assertEquals(followeeUser.image, result.image)
            assertFalse(result.following)

            verify(exactly = 1) { userProviderService.getUser(followerUserId.value) }
            verify(exactly = 1) { userProviderService.getUser(followeeUsername) }
            verify(exactly = 1) { followRepository.unFollow(followerUserId, followeeUserId) }
        }
    }
}