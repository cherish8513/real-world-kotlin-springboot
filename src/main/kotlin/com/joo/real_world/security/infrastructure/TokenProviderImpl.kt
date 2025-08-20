package com.joo.real_world.security.infrastructure

import io.jsonwebtoken.Jwts
import io.jsonwebtoken.io.Decoders
import io.jsonwebtoken.security.Keys
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Component
import java.util.*
import javax.crypto.SecretKey

@Component
class TokenProviderImpl(
    @Value("\${jwt.secret}") private val secretKey: String,
    @Value("\${jwt.expiration}") private val expiration: Long
) : TokenProvider {
    private val signingKey: SecretKey by lazy {
        val keyBytes = Decoders.BASE64.decode(secretKey)
        Keys.hmacShaKeyFor(keyBytes)
    }

    override fun generateToken(userId: Long, username: String, email: String): String {
        return Jwts.builder()
            .subject(userId.toString())
            .claim("username", username)
            .claim("email", email)
            .issuedAt(Date())
            .expiration(Date(System.currentTimeMillis() + expiration))
            .signWith(signingKey)
            .compact()
    }

    override fun extractUserId(token: String): Long {
        return Jwts.parser()
            .verifyWith(signingKey)
            .build()
            .parseSignedClaims(token)
            .payload
            .subject
            .toLong()
    }

    override fun isTokenValid(token: String): Boolean {
        return try {
            val claims = Jwts.parser().verifyWith(signingKey).build().parseSignedClaims(token)
            claims.payload.expiration.after(Date())
        } catch (e: Exception) {
            false
        }
    }
}
