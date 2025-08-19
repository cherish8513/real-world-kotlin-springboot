package com.joo.real_world.user.domain.value

import com.joo.real_world.common.exception.CustomException
import com.joo.real_world.common.exception.CustomExceptionType
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import kotlin.test.assertEquals
import kotlin.test.assertNotEquals

class EmailTest {

    @Test
    fun `should create email when given valid email format`() {
        // Given
        val validEmails = listOf(
            "test@example.com",
            "user.name@domain.co.kr",
            "user+tag@example.org",
            "user_name@example.net",
            "123@example.com",
            "test-user@sub.domain.com",
            "a@b.c",
            "very.long.email.address@very.long.domain.name.com"
        )

        // When & Then
        validEmails.forEach { emailString ->
            val email = Email.of(emailString)
            assertEquals(emailString, email.value)
        }
    }

    @Test
    fun `should throw exception when given invalid email format`() {
        // Given
        val invalidEmails = listOf(
            "",                    // empty string
            "plainaddress",        // missing @
            "@missingdomain",      // missing local part
            "missing@",            // missing domain
            "missing.domain@",     // missing domain
            "spaces @example.com", // contains space
            "test@",               // missing domain part
            "test@@example.com",   // duplicate @
            "test@.com",           // domain starts with dot
            "test@com.",           // domain ends with dot
            "test@ex ample.com"    // space in domain
        )

        // When & Then
        invalidEmails.forEach { invalidEmail ->
            val exception = assertThrows<CustomException> {
                Email.of(invalidEmail)
            }
            assertEquals(CustomExceptionType.INVALID_EMAIL_FORMAT.message, exception.message)
        }
    }

    @Test
    fun `should be equal when created with same email string`() {
        // Given
        val emailString = "test@example.com"

        // When
        val email1 = Email.of(emailString)
        val email2 = Email.of(emailString)

        // Then
        assertEquals(email1, email2)
        assertEquals(email1.hashCode(), email2.hashCode())
    }

    @Test
    fun `should not be equal when created with different email strings`() {
        // Given
        val email1 = Email.of("test1@example.com")
        val email2 = Email.of("test2@example.com")

        // Then
        assertNotEquals(email1, email2)
    }

    @Test
    fun `should return email string when toString is called`() {
        // Given
        val emailString = "test@example.com"
        val email = Email.of(emailString)

        // When & Then
        assertEquals(emailString, email.toString())
    }

    @Test
    fun `should create email when given valid email with special characters`() {
        // Given
        val specialCharEmails = listOf(
            "test+123@example.com",
            "user.name+tag@example.com",
            "user_name@example.com",
            "user-name@example.com",
            "123456@example.com",
            "test.email.with+symbol@example.com"
        )

        // When & Then
        specialCharEmails.forEach { emailString ->
            val email = Email.of(emailString)
            assertEquals(emailString, email.value)
        }
    }

    @Test
    fun `should create email when given mixed case email`() {
        // Given
        val mixedCaseEmails = listOf(
            "Test@Example.Com",
            "USER@DOMAIN.COM",
            "MixedCase@example.org"
        )

        // When & Then
        mixedCaseEmails.forEach { emailString ->
            val email = Email.of(emailString)
            assertEquals(emailString, email.value)
        }
    }

    @Test
    fun `should create email when given email with numeric values`() {
        // Given
        val numericEmails = listOf(
            "123@example.com",
            "user123@example.com",
            "test@123domain.com",
            "user@example123.com"
        )

        // When & Then
        numericEmails.forEach { emailString ->
            val email = Email.of(emailString)
            assertEquals(emailString, email.value)
        }
    }

    @Test
    fun `should create email when given email with subdomain`() {
        // Given
        val subdomainEmails = listOf(
            "test@mail.example.com",
            "user@sub.domain.co.kr",
            "admin@dev.staging.company.org"
        )

        // When & Then
        subdomainEmails.forEach { emailString ->
            val email = Email.of(emailString)
            assertEquals(emailString, email.value)
        }
    }
}