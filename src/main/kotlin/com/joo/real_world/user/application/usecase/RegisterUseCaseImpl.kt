package com.joo.real_world.user.application.usecase

import com.joo.real_world.common.exception.CustomExceptionType
import com.joo.real_world.user.application.UserDto
import com.joo.real_world.user.application.toUserDto
import com.joo.real_world.user.domain.User
import com.joo.real_world.user.domain.UserRepository
import com.joo.real_world.user.domain.vo.Email
import com.joo.real_world.user.domain.vo.Password
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Transactional(rollbackFor = [Exception::class])
@Service
class RegisterUseCaseImpl(
    private val passwordEncoder: PasswordEncoder,
    private val userRepository: UserRepository
) : RegisterUseCase {
    override fun register(username: String, email: String, password: String): UserDto {
        validateDuplicateUser(email = email, username = username)

        return userRepository.save(
            User(
                username = username,
                email = Email.of(email),
                password = Password.of(passwordEncoder.encode(password))
            )
        ).toUserDto()
    }

    private fun validateDuplicateUser(email: String, username: String) {
        if (userRepository.findByEmail(Email.of(email)) != null)
            throw CustomExceptionType.DUPLICATE_EMAIL_EXIST.toException()
        if (userRepository.findByUsername(username) != null)
            throw CustomExceptionType.DUPLICATE_NAME_EXIST.toException()
    }
}