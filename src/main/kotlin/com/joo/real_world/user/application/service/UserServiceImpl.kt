package com.joo.real_world.user.application.service

import com.joo.real_world.user.domain.User
import com.joo.real_world.user.domain.value.Email
import com.joo.real_world.user.domain.value.Password
import com.joo.real_world.user.infrastructure.UserRepository
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Transactional(rollbackFor = [Exception::class])
@Service
class UserServiceImpl(
    private val passwordEncoder: PasswordEncoder,
    private val userRepository: UserRepository
) : UserService {
    override fun register(username: String, email: String, password: String) {
        userRepository.save(
            User(
                username = username,
                email = Email.of(email),
                password = Password.of(passwordEncoder.encode(password))
            )
        )
    }

    override fun getUser(userId: String): User {
        TODO("Not yet implemented")
    }

}