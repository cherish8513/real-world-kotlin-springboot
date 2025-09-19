package com.joo.real_world.security.infrastructure

import com.joo.real_world.common.exception.CustomExceptionType
import com.joo.real_world.common.util.assertNotNull
import com.joo.real_world.user.application.UserQueryService
import org.springframework.stereotype.Service

@Service
class AuthServiceImpl(
    private val userQueryService: UserQueryService,
    private val tokenProvider: TokenProvider
) : AuthService {
    override fun login(userId: Long): String {
        val user = userQueryService.getUser(userId).assertNotNull(CustomExceptionType.LOGIN_REQUIRED)
        return tokenProvider.generateToken(user.id, user.username, user.email)
    }

    override fun getUserSession(token: String): UserSession {
        val userId = tokenProvider.extractUserId(token)
        val user = userQueryService.getUser(userId).assertNotNull(CustomExceptionType.LOGIN_REQUIRED)

        return UserSession(
            userId = user.id,
            username = user.username,
            email = user.email
        )
    }
}
