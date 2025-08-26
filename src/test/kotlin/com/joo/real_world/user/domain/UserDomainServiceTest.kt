package com.joo.real_world.user.domain

import com.joo.real_world.common.exception.CustomExceptionType
import com.joo.real_world.user.domain.vo.Email
import com.joo.real_world.user.domain.vo.Password
import io.mockk.every
import io.mockk.mockk
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import kotlin.test.assertFailsWith

class UserDomainServiceTest {

    private val userRepository: UserRepository = mockk()
    private val userDomainService = UserDomainService(userRepository)

    private val dummyUser = User(
        id = null,
        email = Email.of("email@com"),
        username = "dummy",
        password = Password.of("1"),
        bio = "test",
        image = "no"
    )

    @Test
    fun `email exist - throw DUPLICATE_EMAIL_EXIST`() {
        // given
        val email = "test@example.com"
        val username = "john"
        every { userRepository.findByEmail(Email.of(email)) } returns dummyUser
        every { userRepository.findByUsername(username) } returns null

        // when & then
        val exception = assertFailsWith<RuntimeException> {
            userDomainService.validateDuplicateUser(email, username)
        }
        assertEquals(CustomExceptionType.DUPLICATE_EMAIL_EXIST.toException()::class, exception::class)
    }

    @Test
    fun `username exist - throw DUPLICATE_NAME_EXIST`() {
        // given
        val email = "test@example.com"
        val username = "john"
        every { userRepository.findByEmail(Email.of(email)) } returns null
        every { userRepository.findByUsername(username) } returns dummyUser

        // when & then
        val exception = assertThrows<RuntimeException> {
            userDomainService.validateDuplicateUser(email, username)
        }
        assertEquals(CustomExceptionType.DUPLICATE_NAME_EXIST.toException()::class, exception::class)
    }

    @Test
    fun `success to validate`() {
        // given
        val email = "test@example.com"
        val username = "john"
        every { userRepository.findByEmail(Email.of(email)) } returns null
        every { userRepository.findByUsername(username) } returns null

        // when & then
        userDomainService.validateDuplicateUser(email, username) // no exception
    }
}
