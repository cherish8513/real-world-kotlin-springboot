package com.joo.real_world.security.filter

import com.joo.real_world.security.AuthService
import com.joo.real_world.security.JwtService
import com.joo.real_world.security.UserSession
import io.mockk.every
import io.mockk.mockk
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Test
import org.springframework.mock.web.MockFilterChain
import org.springframework.mock.web.MockHttpServletRequest
import org.springframework.mock.web.MockHttpServletResponse
import org.springframework.security.core.context.SecurityContextHolder
import kotlin.test.assertEquals
import kotlin.test.assertNotNull
import kotlin.test.assertNull

class JwtFilterTest {
    private val jwtService: JwtService = mockk()
    private val authService: AuthService = mockk()
    private val jwtFilter = JwtFilter(jwtService, authService)

    @AfterEach
    fun cleanup() {
        SecurityContextHolder.clearContext()
    }

    @Test
    fun `valid token should set SecurityContext authentication`() {
        // given
        val token = "validToken"
        val userSession = UserSession(
            userId = 1L,
            email = "test@email.com",
            username = "tester"
        )
        val request = MockHttpServletRequest().apply {
            addHeader("Authorization", "Bearer $token")
        }
        val response = MockHttpServletResponse()
        val filterChain = MockFilterChain()

        every { jwtService.isTokenValid(token) } returns true
        every { authService.getUserSession(token) } returns userSession

        // when
        jwtFilter.doFilter(request, response, filterChain)

        // then
        val authentication = SecurityContextHolder.getContext().authentication
        assertNotNull(authentication)
        assertEquals(userSession, authentication.principal)
        assertEquals(0, authentication.authorities.size)
    }

    @Test
    fun `invalid token should not set SecurityContext authentication`() {
        // given
        val token = "invalidToken"
        val request = MockHttpServletRequest().apply {
            addHeader("Authorization", "Bearer $token")
        }
        val response = MockHttpServletResponse()
        val filterChain = MockFilterChain()

        every { jwtService.isTokenValid(token) } returns false

        // when
        jwtFilter.doFilter(request, response, filterChain)

        // then
        val authentication = SecurityContextHolder.getContext().authentication
        assertNull(authentication)
    }

    @Test
    fun `no token should not set SecurityContext authentication`() {
        // given
        val request = MockHttpServletRequest() // 헤더 없음
        val response = MockHttpServletResponse()
        val filterChain = MockFilterChain()

        // when
        jwtFilter.doFilter(request, response, filterChain)

        // then
        val authentication = SecurityContextHolder.getContext().authentication
        assertNull(authentication)
    }
}
