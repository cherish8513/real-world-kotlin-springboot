package com.joo.real_world.user.application.service

import com.joo.real_world.common.exception.CustomExceptionType
import com.joo.real_world.user.application.ModifyUserDto
import com.joo.real_world.user.domain.User
import com.joo.real_world.user.domain.UserRepository
import com.joo.real_world.user.domain.value.Email
import com.joo.real_world.user.domain.value.Password
import com.joo.real_world.user.domain.value.UserId
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.springframework.security.crypto.password.PasswordEncoder

class UserServiceImplTest {

    private lateinit var userRepository: UserRepository
    private lateinit var passwordEncoder: PasswordEncoder
    private lateinit var userService: UserServiceImpl

    @BeforeEach
    fun setUp() {
        userRepository = mockk()
        passwordEncoder = mockk()
        userService = UserServiceImpl(passwordEncoder, userRepository)
    }

    @Test
    fun `success to register`() {
        // given
        val email = "test@test.com"
        val username = "tester"
        val rawPassword = "password"
        val encodedPassword = "encodedPw"

        val user =
            User(id = UserId(1L), email = Email.of(email), username = username, password = Password.of(encodedPassword))

        every { userRepository.findByEmail(Email.of(email)) } returns null
        every { passwordEncoder.encode(rawPassword) } returns encodedPassword
        every { userRepository.save(any()) } returns user

        // when
        val result = userService.register(username, email, rawPassword)

        // then
        assertEquals(username, result.username)
        assertEquals(email, result.email)
        verify { userRepository.save(any()) }
    }

    @Test
    fun `should fail to register - duplicate`() {
        val email = "duplicate@test.com"
        every { userRepository.findByEmail(Email.of(email)) } returns mockk<User>()

        // when & then
        val ex = assertThrows<RuntimeException> {
            userService.register("tester", email, "pw")
        }
        assertEquals(CustomExceptionType.DUPLICATE_USER_EXIST.toException()::class, ex::class)
    }

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
        val result = userService.getUser(email, rawPassword)

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
            userService.getUser(email, rawPassword)
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
            userService.getUser(email, rawPassword)
        }
        assertEquals(CustomExceptionType.INVALID_USER.toException()::class, ex::class)
    }

    @Test
    fun `success to modify user`() {
        // given
        val beforeUser = User(
            id = UserId(1L),
            email = Email.of("before@before.com"),
            username = "tester",
            password = Password.of("beforePw")
        )
        val modifyDto = ModifyUserDto(
            id = 1L,
            email = "after@test.com",
            username = "after",
            password = "newPw",
            bio = "bio",
            image = "image"
        )
        val encodedPassword = "encodedNewPw"

        every { userRepository.findByUserId(UserId(modifyDto.id)) } returns beforeUser
        every { passwordEncoder.encode(modifyDto.password!!) } returns encodedPassword
        every { userRepository.save(any()) } answers { firstArg<User>() }

        // when
        val result = userService.modifyUser(modifyDto)

        // then
        assertEquals(modifyDto.username, result.username)
        assertEquals(modifyDto.email, result.email)
    }
}
