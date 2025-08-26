package com.joo.real_world.user.domain

import com.joo.real_world.common.exception.CustomExceptionType
import com.joo.real_world.user.domain.vo.Email
import org.springframework.stereotype.Component

@Component
class UserDomainService(
    private val userRepository: UserRepository
) {
    fun validateDuplicateUser(email: String, username: String) {
        if (userRepository.findByEmail(Email.of(email)) != null)
            throw CustomExceptionType.DUPLICATE_EMAIL_EXIST.toException()
        if (userRepository.findByUsername(username) != null)
            throw CustomExceptionType.DUPLICATE_NAME_EXIST.toException()
    }
}