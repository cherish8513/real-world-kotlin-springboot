package com.joo.real_world

import com.fasterxml.jackson.databind.ObjectMapper
import com.joo.real_world.security.application.AuthService
import com.joo.real_world.security.application.JwtService
import com.joo.real_world.security.application.UserSession
import com.ninjasquad.springmockk.MockkBean
import org.junit.jupiter.api.BeforeEach
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.security.authentication.TestingAuthenticationToken
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.test.web.servlet.MockMvc


abstract class AbstractControllerTest {
    @Autowired
    protected lateinit var mockMvc: MockMvc

    @MockkBean
    protected lateinit var authService: AuthService  // private → protected로 변경

    @MockkBean
    protected lateinit var jwtService: JwtService    // private → protected로 변경

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