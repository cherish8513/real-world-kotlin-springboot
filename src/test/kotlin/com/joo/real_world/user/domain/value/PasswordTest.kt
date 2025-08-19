package com.joo.real_world.user.domain.value

import org.junit.jupiter.api.Test
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.springframework.security.crypto.password.PasswordEncoder
import kotlin.test.assertEquals
import kotlin.test.assertNotEquals
import kotlin.test.assertTrue
import kotlin.test.assertFalse

class PasswordTest {

    private val passwordEncoder = mockk<PasswordEncoder>()

    @Test
    fun `should create password when given encoded string`() {
        // Given
        val encodedPassword = "encoded_password_hash_12345"

        // When
        val password = Password.of(encodedPassword)

        // Then
        assertEquals(encodedPassword, password.value)
    }

    @Test
    fun `should create password when given empty string`() {
        // Given
        val emptyPassword = ""

        // When
        val password = Password.of(emptyPassword)

        // Then
        assertEquals(emptyPassword, password.value)
    }

    @Test
    fun `should return true when raw password matches encoded password`() {
        // Given
        val rawPassword = "myPassword123"
        val encodedPassword = "encoded_hash_of_myPassword123"
        val password = Password.of(encodedPassword)

        every { passwordEncoder.matches(rawPassword, encodedPassword) } returns true

        // When
        val result = password.matches(rawPassword, passwordEncoder)

        // Then
        assertTrue(result)
        verify { passwordEncoder.matches(rawPassword, encodedPassword) }
    }

    @Test
    fun `should return false when raw password does not match encoded password`() {
        // Given
        val rawPassword = "wrongPassword"
        val encodedPassword = "encoded_hash_of_myPassword123"
        val password = Password.of(encodedPassword)

        every { passwordEncoder.matches(rawPassword, encodedPassword) } returns false

        // When
        val result = password.matches(rawPassword, passwordEncoder)

        // Then
        assertFalse(result)
        verify { passwordEncoder.matches(rawPassword, encodedPassword) }
    }

    @Test
    fun `should return false when raw password is empty and encoded is not`() {
        // Given
        val rawPassword = ""
        val encodedPassword = "encoded_hash_12345"
        val password = Password.of(encodedPassword)

        every { passwordEncoder.matches(rawPassword, encodedPassword) } returns false

        // When
        val result = password.matches(rawPassword, passwordEncoder)

        // Then
        assertFalse(result)
        verify { passwordEncoder.matches(rawPassword, encodedPassword) }
    }

    @Test
    fun `should return true when both raw and encoded passwords are empty and encoder returns true`() {
        // Given
        val rawPassword = ""
        val encodedPassword = ""
        val password = Password.of(encodedPassword)

        every { passwordEncoder.matches(rawPassword, encodedPassword) } returns true

        // When
        val result = password.matches(rawPassword, passwordEncoder)

        // Then
        assertTrue(result)
        verify { passwordEncoder.matches(rawPassword, encodedPassword) }
    }

    @Test
    fun `should be equal when created with same encoded string`() {
        // Given
        val encodedPassword = "same_encoded_hash_12345"

        // When
        val password1 = Password.of(encodedPassword)
        val password2 = Password.of(encodedPassword)

        // Then
        assertEquals(password1, password2)
        assertEquals(password1.hashCode(), password2.hashCode())
    }

    @Test
    fun `should not be equal when created with different encoded strings`() {
        // Given
        val password1 = Password.of("encoded_hash_1")
        val password2 = Password.of("encoded_hash_2")

        // Then
        assertNotEquals(password1, password2)
    }

    @Test
    fun `should return encoded password string when toString is called`() {
        // Given
        val encodedPassword = "encoded_password_hash"
        val password = Password.of(encodedPassword)

        // When & Then
        assertEquals(encodedPassword, password.toString())
    }

    @Test
    fun `should handle special characters in encoded password`() {
        // Given
        val encodedWithSpecialChars = "$2a$10$.zmdr9k7uOCQb376NoUnuTJ8iYqiSfFUp4hjqpVZ0pQUqoCW9Eze"

        // When
        val password = Password.of(encodedWithSpecialChars)

        // Then
        assertEquals(encodedWithSpecialChars, password.value)
    }

    @Test
    fun `should delegate password matching to encoder correctly`() {
        // Given
        val rawPassword = "testPassword"
        val encodedPassword = "hashed_test_password"
        val password = Password.of(encodedPassword)

        every { passwordEncoder.matches(rawPassword, encodedPassword) } returns true

        // When
        val result = password.matches(rawPassword, passwordEncoder)

        // Then
        assertTrue(result)
        verify(exactly = 1) { passwordEncoder.matches(rawPassword, encodedPassword) }
    }

    @Test
    fun `should work with different password matching scenarios`() {
        // Given
        val rawPassword = "mySecretPassword"
        val wrongPassword = "wrongPassword"
        val encodedPassword = "bcrypt_encoded_password"
        val password = Password.of(encodedPassword)

        every { passwordEncoder.matches(rawPassword, encodedPassword) } returns true
        every { passwordEncoder.matches(wrongPassword, encodedPassword) } returns false

        // When & Then
        assertTrue(password.matches(rawPassword, passwordEncoder))
        assertFalse(password.matches(wrongPassword, passwordEncoder))

        verify(exactly = 1) { passwordEncoder.matches(rawPassword, encodedPassword) }
        verify(exactly = 1) { passwordEncoder.matches(wrongPassword, encodedPassword) }
    }

    @Test
    fun `should handle multiple calls with same password encoder`() {
        // Given
        val rawPassword = "consistentPassword"
        val encodedPassword = "consistent_encoded_hash"
        val password = Password.of(encodedPassword)

        every { passwordEncoder.matches(rawPassword, encodedPassword) } returns true

        // When
        val result1 = password.matches(rawPassword, passwordEncoder)
        val result2 = password.matches(rawPassword, passwordEncoder)

        // Then
        assertTrue(result1)
        assertTrue(result2)
        verify(exactly = 2) { passwordEncoder.matches(rawPassword, encodedPassword) }
    }

    @Test
    fun `should handle long encoded passwords`() {
        // Given
        val longEncodedPassword = "a".repeat(200)
        val password = Password.of(longEncodedPassword)

        // When & Then
        assertEquals(longEncodedPassword, password.value)
        assertEquals(longEncodedPassword, password.toString())
    }

    @Test
    fun `should verify encoder is called with correct parameters`() {
        // Given
        val rawPassword = "verificationTest"
        val encodedPassword = "verification_encoded"
        val password = Password.of(encodedPassword)

        every { passwordEncoder.matches(any(), any()) } returns true

        // When
        password.matches(rawPassword, passwordEncoder)

        // Then
        verify { passwordEncoder.matches(rawPassword, encodedPassword) }
        verify(exactly = 1) { passwordEncoder.matches(any(), any()) }
    }
}