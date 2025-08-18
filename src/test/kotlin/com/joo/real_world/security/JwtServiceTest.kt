package com.joo.real_world.security

import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import java.util.*
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertTrue

class JwtServiceTest {
    private lateinit var jwtService: JwtService

    private val secret = Base64.getEncoder().encodeToString("my-secret-key-which-is-long-enough".toByteArray())
    private val expiration = 1000L * 60 * 60

    @BeforeEach
    fun setUp() {
        jwtService = JwtService(secret, expiration)
    }

    @Test
    fun `generateToken should create a valid JWT token`() {
        val token = jwtService.generateToken(1L, "tester", "test@email.com")
        assertTrue(token.isNotEmpty())
        assertTrue(jwtService.isTokenValid(token))
    }

    @Test
    fun `extractUserId should return correct userId`() {
        val userId = 123L
        val token = jwtService.generateToken(userId, "tester", "test@email.com")
        val extractedId = jwtService.extractUserId(token)
        assertEquals(userId, extractedId)
    }

    @Test
    fun `isTokenValid should return false for expired token`() {
        val shortExpirationService = JwtService(secret, 1L) // 1ms
        val token = shortExpirationService.generateToken(1L, "tester", "test@email.com")

        Thread.sleep(10) // 토큰 만료 대기

        assertFalse(shortExpirationService.isTokenValid(token))
    }

    @Test
    fun `isTokenValid should return false for invalid token`() {
        val invalidToken = "invalid.token.value"
        assertFalse(jwtService.isTokenValid(invalidToken))
    }
}
