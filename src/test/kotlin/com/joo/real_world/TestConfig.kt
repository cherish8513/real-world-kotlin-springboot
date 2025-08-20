package com.joo.real_world

import com.joo.real_world.security.infrastructure.UserSession
import org.springframework.boot.test.context.TestConfiguration
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Primary

@TestConfiguration
class TestConfig {

    @Bean
    @Primary
    fun testUserSession(): UserSession {
        return UserSession(
            userId = 1L,
            username = "testuser",
            email = "test@example.com"
        )
    }
}