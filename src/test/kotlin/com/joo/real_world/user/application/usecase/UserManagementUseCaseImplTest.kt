package com.joo.real_world.user.application.usecase

import com.joo.real_world.user.application.ModifyUserDto
import com.joo.real_world.user.domain.User
import com.joo.real_world.user.domain.UserDomainService
import com.joo.real_world.user.domain.UserRepository
import com.joo.real_world.user.domain.vo.Email
import com.joo.real_world.user.domain.vo.Password
import com.joo.real_world.user.domain.vo.UserId
import io.mockk.*
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import org.springframework.security.crypto.password.PasswordEncoder

class UserManagementUseCaseImplTest {
    private val userRepository: UserRepository = mockk()
    private val passwordEncoder: PasswordEncoder = mockk()
    private val userDomainService: UserDomainService = mockk()
    private val userManagementUseCase: UserManagementUseCase =
        UserManagementUseCaseImpl(passwordEncoder, userRepository, userDomainService)

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
        every { userRepository.findByUsername(username) } returns null
        every { passwordEncoder.encode(rawPassword) } returns encodedPassword
        every { userRepository.save(any()) } returns user
        every { userDomainService.validateDuplicateUser(email, username) } just runs

        // when
        val result = userManagementUseCase.register(username, email, rawPassword)

        // then
        assertEquals(username, result.username)
        assertEquals(email, result.email)
        verify { userRepository.save(any()) }
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
        val result = userManagementUseCase.modifyUser(modifyDto)

        // then
        assertEquals(modifyDto.username, result.username)
        assertEquals(modifyDto.email, result.email)
    }
}
