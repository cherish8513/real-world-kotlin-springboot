package com.joo.real_world.user.application.usecase

import com.joo.real_world.common.exception.CustomException
import com.joo.real_world.common.exception.CustomExceptionType
import com.joo.real_world.user.domain.FollowRepository
import com.joo.real_world.user.domain.User
import com.joo.real_world.user.domain.UserRepository
import com.joo.real_world.user.domain.vo.Email
import com.joo.real_world.user.domain.vo.Password
import com.joo.real_world.user.domain.vo.UserId
import io.mockk.*
import org.junit.jupiter.api.*
import org.junit.jupiter.api.Assertions.*

class FollowManagementUseCaseImplTest {

    private val followRepository: FollowRepository = mockk()
    private val userRepository: UserRepository = mockk()

    private val useCase = FollowManagementUseCaseImpl(
        followRepository = followRepository,
        userRepository = userRepository
    )

    private val followerId = 1L
    private val followerUserId = UserId(followerId)
    private val followeeUsername = "testuser"
    private val followeeId = 2L
    private val followeeUserId = UserId(followeeId)

    private val followerUser = User(
        id = followerUserId,
        password = Password.of("1"),
        username = "follower",
        email = Email.of("follower@test.com"),
        bio = "follower bio",
        image = "follower.jpg"
    )

    private val followeeUser = User(
        id = followeeUserId,
        username = followeeUsername,
        password = Password.of("1"),
        email = Email.of("followee@test.com"),
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
            every { userRepository.findByUserId(followerUserId) } returns followerUser
            every { userRepository.findByUsername(followeeUsername) } returns followeeUser
            every { followRepository.isFollowing(followerUserId, followeeUserId) } returns false
            every { followRepository.follow(any()) } returns mockk()

            // when
            val result = useCase.follow(followerId, followeeUsername)

            // then
            assertEquals(followeeUsername, result.username)
            assertEquals(followeeUser.bio, result.bio)
            assertEquals(followeeUser.image, result.image)
            assertTrue(result.following)

            verify(exactly = 1) { userRepository.findByUserId(followerUserId) }
            verify(exactly = 1) { userRepository.findByUsername(followeeUsername) }
            verify(exactly = 1) { followRepository.isFollowing(followerUserId, followeeUserId) }
            verify(exactly = 1) { followRepository.follow(any()) }
        }

        @Test
        @DisplayName("팔로위가 존재하지 않으면 INVALID_USER 예외가 발생한다")
        fun `should throw INVALID_USER exception when followee not found`() {
            // given
            every { userRepository.findByUserId(followerUserId) } returns followerUser
            every { userRepository.findByUsername(followeeUsername) } returns null

            // when & then
            val exception = assertThrows<CustomException> {
                useCase.follow(followerId, followeeUsername)
            }

            assertEquals(CustomExceptionType.INVALID_USER, exception.exceptionType)

            verify(exactly = 1) { userRepository.findByUserId(followerUserId) }
            verify(exactly = 1) { userRepository.findByUsername(followeeUsername) }
        }

        @Test
        @DisplayName("이미 팔로우 중인 사용자를 팔로우하면 ALREADY_FOLLOW 예외가 발생한다")
        fun `should throw ALREADY_FOLLOW exception when already following`() {
            // given
            every { userRepository.findByUserId(followerUserId) } returns followerUser
            every { userRepository.findByUsername(followeeUsername) } returns followeeUser
            every { followRepository.isFollowing(followerUserId, followeeUserId) } returns true

            // when & then
            val exception = assertThrows<CustomException> {
                useCase.follow(followerId, followeeUsername)
            }

            assertEquals(CustomExceptionType.ALREADY_FOLLOW, exception.exceptionType)

            verify(exactly = 1) { followRepository.isFollowing(followerUserId, followeeUserId) }
            verify(exactly = 0) { followRepository.follow(any()) }
        }

        @Test
        @DisplayName("자기 자신을 팔로우하려고 하면 UNEXPECTED_ERROR_OCCURRED 예외가 발생한다")
        fun `should throw UNEXPECTED_ERROR_OCCURRED exception when trying to follow self`() {
            // given
            val sameUserId = UserId(followerId)
            val sameUser = User(
                id = sameUserId,
                username = followeeUsername,
                email = Email.of("same@test.com"),
                bio = "same bio",
                image = "same.jpg",
                password = Password.of("1")
            )

            every { userRepository.findByUserId(followerUserId) } returns followerUser
            every { userRepository.findByUsername(followeeUsername) } returns sameUser

            // when & then
            val exception = assertThrows<CustomException> {
                useCase.follow(followerId, followeeUsername)
            }

            assertEquals(CustomExceptionType.UNEXPECTED_ERROR_OCCURRED, exception.exceptionType)
        }
    }

    @Nested
    @DisplayName("언팔로우 기능")
    inner class UnfollowTest {

        @Test
        @DisplayName("정상적으로 언팔로우를 할 수 있다")
        fun `should unfollow user successfully`() {
            // given
            every { userRepository.findByUserId(followerUserId) } returns followerUser
            every { userRepository.findByUsername(followeeUsername) } returns followeeUser
            every { followRepository.isFollowing(followerUserId, followeeUserId) } returns true
            every { followRepository.unFollow(followerUserId, followeeUserId) } just Runs

            // when
            val result = useCase.unfollow(followerId, followeeUsername)

            // then
            assertEquals(followeeUsername, result.username)
            assertEquals(followeeUser.bio, result.bio)
            assertEquals(followeeUser.image, result.image)
            assertFalse(result.following)

            verify(exactly = 1) { userRepository.findByUserId(followerUserId) }
            verify(exactly = 1) { userRepository.findByUsername(followeeUsername) }
            verify(exactly = 1) { followRepository.isFollowing(followerUserId, followeeUserId) }
            verify(exactly = 1) { followRepository.unFollow(followerUserId, followeeUserId) }
        }

        @Test
        @DisplayName("팔로위가 존재하지 않으면 INVALID_USER 예외가 발생한다")
        fun `should throw INVALID_USER exception when followee not found in unfollow`() {
            // given
            every { userRepository.findByUserId(followerUserId) } returns followerUser
            every { userRepository.findByUsername(followeeUsername) } returns null

            // when & then
            val exception = assertThrows<CustomException> {
                useCase.unfollow(followerId, followeeUsername)
            }

            assertEquals(CustomExceptionType.INVALID_USER, exception.exceptionType)
        }

        @Test
        @DisplayName("팔로우하지 않은 사용자를 언팔로우하려고 하면 NOT_FOLLOW 예외가 발생한다")
        fun `should throw NOT_FOLLOW exception when not following`() {
            // given
            every { userRepository.findByUserId(followerUserId) } returns followerUser
            every { userRepository.findByUsername(followeeUsername) } returns followeeUser
            every { followRepository.isFollowing(followerUserId, followeeUserId) } returns false

            // when & then
            val exception = assertThrows<CustomException> {
                useCase.unfollow(followerId, followeeUsername)
            }

            assertEquals(CustomExceptionType.NOT_FOLLOW, exception.exceptionType)

            verify(exactly = 1) { followRepository.isFollowing(followerUserId, followeeUserId) }
            verify(exactly = 0) { followRepository.unFollow(any(), any()) }
        }

        @Test
        @DisplayName("자기 자신을 언팔로우하려고 하면 UNEXPECTED_ERROR_OCCURRED 예외가 발생한다")
        fun `should throw UNEXPECTED_ERROR_OCCURRED exception when trying to unfollow self`() {
            // given
            val sameUserId = UserId(followerId)
            val sameUser = User(
                id = sameUserId,
                username = followeeUsername,
                email = Email.of("same@test.com"),
                bio = "same bio",
                image = "same.jpg",
                password = Password.of("1")
            )

            every { userRepository.findByUserId(followerUserId) } returns followerUser
            every { userRepository.findByUsername(followeeUsername) } returns sameUser

            // when & then
            val exception = assertThrows<CustomException> {
                useCase.unfollow(followerId, followeeUsername)
            }

            assertEquals(CustomExceptionType.UNEXPECTED_ERROR_OCCURRED, exception.exceptionType)
        }
    }
}