package com.joo.real_world.security

import com.joo.real_world.common.exception.CustomExceptionType
import com.joo.real_world.common.util.assertNotNull
import com.joo.real_world.user.application.service.UserService
import io.jsonwebtoken.Jwts
import io.jsonwebtoken.io.Decoders
import io.jsonwebtoken.security.Keys
import org.springframework.stereotype.Service
import java.util.*
import javax.crypto.SecretKey


@Service
class AuthService(
    private val userService: UserService
) {
    private val expiration = 60 * 30 * 1000 // 30 minutes
    private val signingKey: SecretKey = getSigningKey()

    private fun getSigningKey(): SecretKey {
        val secretKey = "9a4f2c8d3b7a1e6f45c8a0b3f267d8b1d4e6f3c8a9d2b5f8e3a9c8b5f6v8a3d9"
        val keyBytes = Decoders.BASE64.decode(secretKey)
        return Keys.hmacShaKeyFor(keyBytes)
    }

    fun generateToken(subject: String): String {
        return Jwts.builder()
            .signWith(signingKey)
            .subject(subject)
            .issuedAt(Date())
            .expiration(Date(System.currentTimeMillis() + expiration))
            .compact()
    }

    fun extractUser(token: String): UserSession {
        val userId = Jwts.parser().verifyWith(signingKey).build().parseSignedClaims(token).payload.subject
        val user = userService.getUser(userId).assertNotNull(CustomExceptionType.LOGIN_REQUIRED)
        return UserSession()
    }
}