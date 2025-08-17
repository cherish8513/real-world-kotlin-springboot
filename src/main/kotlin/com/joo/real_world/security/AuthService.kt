package com.joo.real_world.security

import com.joo.real_world.common.exception.CustomExceptionType
import com.joo.real_world.common.util.assertNotNull
import com.joo.real_world.user.application.service.UserService
import org.springframework.stereotype.Service

@Service
class AuthService(
    private val userService: UserService,
    private val jwtService: JwtService
) {
    fun login(userId: Long): String {
        val user = userService.getUser(userId).assertNotNull(CustomExceptionType.LOGIN_REQUIRED)
        return jwtService.generateToken(user.id, user.username, user.email)
    }

    fun getUserSession(token: String): UserSession {
        val userId = jwtService.extractUserId(token)
        val user = userService.getUser(userId).assertNotNull(CustomExceptionType.LOGIN_REQUIRED)

        return UserSession(
            userId = user.id,
            username = user.username,
            email = user.email
        )
    }
}
