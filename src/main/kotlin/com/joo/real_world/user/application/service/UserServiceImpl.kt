package com.joo.real_world.user.application.service

import com.joo.real_world.common.exception.CustomExceptionType
import com.joo.real_world.common.util.assertNotNull
import com.joo.real_world.user.application.ModifyUserDto
import com.joo.real_world.user.application.UserDto
import com.joo.real_world.user.application.toUserDto
import com.joo.real_world.user.domain.User
import com.joo.real_world.user.domain.UserRepository
import com.joo.real_world.user.domain.value.Email
import com.joo.real_world.user.domain.value.Password
import com.joo.real_world.user.domain.value.UserId
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Transactional(rollbackFor = [Exception::class])
@Service
class UserServiceImpl(
    private val passwordEncoder: PasswordEncoder,
    private val userRepository: UserRepository
) : UserService {
    override fun register(username: String, email: String, password: String): UserDto {
        validateDuplicateUser(email)

        return userRepository.save(
            User(
                username = username,
                email = Email.of(email),
                password = Password.of(passwordEncoder.encode(password))
            )
        ).toUserDto()
    }

    private fun validateDuplicateUser(email: String) {
        if (userRepository.findByEmail(Email.of(email)) != null)
            throw CustomExceptionType.DUPLICATE_USER_EXIST.toException()
    }

    override fun getUser(email: String, password: String): UserDto {
        val user = userRepository.findByEmail(Email.of(email))
            ?.takeIf { it.password.matches(password, passwordEncoder) }
            ?: throw CustomExceptionType.INVALID_USER.toException()

        return user.toUserDto()
    }


    override fun getUser(userId: Long): UserDto {
        return userRepository.findByUserId(UserId(userId))
            .assertNotNull(CustomExceptionType.INVALID_USER)
            .toUserDto()
    }

    override fun modifyUser(modifyUserDto: ModifyUserDto): UserDto {
        val beforeUser = userRepository
            .findByUserId(UserId(modifyUserDto.id))
            .assertNotNull(CustomExceptionType.INVALID_USER)

        return userRepository.save(
            beforeUser.change(
                email = modifyUserDto.email,
                username = modifyUserDto.username,
                password = modifyUserDto.password?.let { Password.of(passwordEncoder.encode(it)) },
                bio = modifyUserDto.bio,
                image = modifyUserDto.image,
            )
        ).toUserDto()
    }

}