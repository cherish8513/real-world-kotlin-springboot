package com.joo.real_world.user.application

import com.joo.real_world.common.exception.CustomExceptionType
import com.joo.real_world.user.domain.User
import com.joo.real_world.user.domain.UserRepository
import com.joo.real_world.user.domain.vo.Email
import com.joo.real_world.user.domain.vo.Password
import com.joo.real_world.user.domain.vo.UserId
import io.mockk.every
import io.mockk.mockk
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows

class UserProviderServiceImplTest {

    private val userRepository: UserRepository = mockk()
    private val userProviderService: UserProviderService = UserProviderServiceImpl(userRepository)

    @Test
    fun `return UserDto when success to get user`() {
        // given
        val user = User(
            id = UserId(1L),
            username = "tester",
            email = Email.of("tester@example.com"),
            password = Password.of("encoded_pw"),
            bio = "bio",
            image = "image.png"
        )
        every { userRepository.findByUserId(UserId(1L)) } returns user

        // when
        val result = userProviderService.getUser(1L)

        // then
        assertEquals(user.id?.value, result.id)
        assertEquals(user.username, result.username)
        assertEquals(user.email.value, result.email)
        assertEquals(user.bio, result.bio)
        assertEquals(user.image, result.image)
    }

    @Test
    fun `occur INVALID_USER when fail to get user`() {
        // given
        every { userRepository.findByUserId(UserId(999L)) } returns null

        // when & then
        val ex = assertThrows<RuntimeException> {
            userProviderService.getUser(999L)
        }
        assertEquals(CustomExceptionType.INVALID_USER.toException()::class, ex::class)
    }

}
