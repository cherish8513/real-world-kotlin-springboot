package com.joo.real_world.user.application.usecase

import com.joo.real_world.common.exception.CustomExceptionType
import com.joo.real_world.user.application.UserDto
import com.joo.real_world.user.application.toUserDto
import com.joo.real_world.user.domain.UserRepository
import com.joo.real_world.user.domain.vo.Email
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Transactional(rollbackFor = [Exception::class])
@Service
class LoginUseCaseImpl(
    private val passwordEncoder: PasswordEncoder,
    private val userRepository: UserRepository
) : LoginUseCase {
    override fun getUser(email: String, password: String): UserDto {
        val user = userRepository.findByEmail(Email.of(email))
            ?: throw CustomExceptionType.INVALID_USER.toException()

        if (!user.password.matches(password, passwordEncoder)) {
            throw CustomExceptionType.PASSWORD_INCORRECT.toException()
        }

        return user.toUserDto()
    }
}