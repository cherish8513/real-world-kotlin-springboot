package com.joo.real_world.user.application.usecase

import com.joo.real_world.user.application.ModifyUserDto
import com.joo.real_world.user.domain.User
import com.joo.real_world.user.domain.UserRepository
import com.joo.real_world.user.domain.vo.Email
import com.joo.real_world.user.domain.vo.Password
import com.joo.real_world.user.domain.vo.UserId
import io.mockk.every
import io.mockk.mockk
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import org.springframework.security.crypto.password.PasswordEncoder

class UpdateUserUseCaseImplTest {

    private val userRepository: UserRepository = mockk()
    private val passwordEncoder: PasswordEncoder = mockk()
    private val updateUserUseCase: UpdateUserUseCase = UpdateUserUseCaseImpl(passwordEncoder, userRepository)

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
        val result = updateUserUseCase.modifyUser(modifyDto)

        // then
        assertEquals(modifyDto.username, result.username)
        assertEquals(modifyDto.email, result.email)
    }
}
