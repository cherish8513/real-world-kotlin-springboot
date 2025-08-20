package com.joo.real_world.user.application.usecase


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
import org.springframework.security.crypto.password.PasswordEncoder

class LoginUseCaseImplTest {

    private val userRepository: UserRepository = mockk()
    private val passwordEncoder: PasswordEncoder = mockk()
    private val loginUseCase: LoginUseCase = LoginUseCaseImpl(passwordEncoder, userRepository)

    @Test
    fun `success to login`() {
        // given
        val email = "login@test.com"
        val rawPassword = "pw"
        val encodedPassword = "encodedPw"
        val user =
            User(id = UserId(1L), email = Email.of(email), username = "tester", password = Password.of(encodedPassword))

        every { userRepository.findByEmail(Email.of(email)) } returns user
        every { user.password.matches(rawPassword, passwordEncoder) } returns true

        // when
        val result = loginUseCase.getUser(email, rawPassword)

        // then
        assertEquals(email, result.email)
    }

    @Test
    fun `should fail to login - invalid email`() {
        // given
        val email = "login@test.com"
        val rawPassword = "wrong"

        every { userRepository.findByEmail(Email.of(email)) } returns null

        // when & then
        val ex = assertThrows<RuntimeException> {
            loginUseCase.getUser(email, rawPassword)
        }
        assertEquals(CustomExceptionType.INVALID_USER.toException()::class, ex::class)
    }

    @Test
    fun `should fail to login - invalid password`() {
        // given
        val email = "login@test.com"
        val rawPassword = "wrong"
        val encodedPassword = "encodedPw"
        val user =
            User(id = null, email = Email.of(email), username = "tester", password = Password.of(encodedPassword))

        every { userRepository.findByEmail(Email.of(email)) } returns user
        every { user.password.matches(rawPassword, passwordEncoder) } returns false

        // when & then
        val ex = assertThrows<RuntimeException> {
            loginUseCase.getUser(email, rawPassword)
        }
        assertEquals(CustomExceptionType.INVALID_USER.toException()::class, ex::class)
    }
}