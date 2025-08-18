package com.joo.real_world

import com.fasterxml.jackson.databind.ObjectMapper
import com.joo.real_world.security.AuthService
import com.joo.real_world.security.JwtService
import com.joo.real_world.security.UserSession
import org.junit.jupiter.api.BeforeEach
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.security.authentication.TestingAuthenticationToken
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.test.context.bean.override.mockito.MockitoBean
import org.springframework.test.web.servlet.MockMvc


abstract class AbstractControllerTest {
    @Autowired
    protected lateinit var mockMvc: MockMvc

    @MockitoBean
    private lateinit var authService: AuthService

    @MockitoBean
    private lateinit var jwtService: JwtService

    lateinit var objectMapper: ObjectMapper

    val testUserSession = UserSession(
        userId = 1L,
        username = "test",
        email = "test@tester.com"
    )

    @BeforeEach
    fun setup() {
        objectMapper = ObjectMapper()
        val authentication = TestingAuthenticationToken(testUserSession, null, listOf())
        SecurityContextHolder.getContext().authentication = authentication
    }
}
