package com.joo.real_world.user.application

import com.joo.real_world.common.exception.CustomExceptionType
import com.joo.real_world.common.util.assertNotNull
import com.joo.real_world.user.domain.UserRepository
import com.joo.real_world.user.domain.vo.UserId
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Transactional(readOnly = true, rollbackFor = [Exception::class])
@Service
class UserProviderServiceImpl(
    private val userRepository: UserRepository
) : UserProviderService {
    override fun getUser(userId: Long): UserDto {
        return userRepository.findByUserId(UserId(userId))
            .assertNotNull(CustomExceptionType.INVALID_USER)
            .toUserDto()
    }
}