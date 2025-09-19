package com.joo.real_world.user.application.usecase

import com.joo.real_world.common.exception.CustomExceptionType
import com.joo.real_world.common.util.assertNotNull
import com.joo.real_world.user.application.ModifyUserDto
import com.joo.real_world.user.application.UserDto
import com.joo.real_world.user.application.toUserDto
import com.joo.real_world.user.domain.User
import com.joo.real_world.user.domain.UserDomainService
import com.joo.real_world.user.domain.UserRepository
import com.joo.real_world.user.domain.vo.Email
import com.joo.real_world.user.domain.vo.Password
import com.joo.real_world.user.domain.vo.UserId
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Transactional(rollbackFor = [Exception::class])
@Service
class UserCommandUseCaseImpl(
    private val passwordEncoder: PasswordEncoder,
    private val userRepository: UserRepository,
    private val userDomainService: UserDomainService
) : UserCommandUseCase {
    override fun register(username: String, email: String, password: String): UserDto {
        userDomainService.validateDuplicateUser(email = email, username = username)

        return userRepository.save(
            User(
                username = username,
                email = Email.of(email),
                password = Password.of(passwordEncoder.encode(password))
            )
        ).toUserDto()
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