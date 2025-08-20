package com.joo.real_world.security.infrastructure

import com.joo.real_world.user.application.UserDto
import com.joo.real_world.user.application.UserProviderService
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith

class AuthServiceImplTest {
    private val userProviderService: UserProviderService = mockk()
    private val tokenProvider: TokenProvider = mockk()
    private val authService = AuthServiceImpl(userProviderService, tokenProvider)

    @Test
    fun `login should return token when user exists`() {
        val user = UserDto(id = 1L, username = "tester", email = "test@email.com", bio = null, image = null)

        every { userProviderService.getUser(1L) } returns user
        every { tokenProvider.generateToken(user.id, user.username, user.email) } returns "token123"

        val token = authService.login(1L)
        assertEquals("token123", token)

        // 호출 확인(Optional)
        verify(exactly = 1) { userProviderService.getUser(1L) }
        verify(exactly = 1) { tokenProvider.generateToken(user.id, user.username, user.email) }
    }

    @Test
    fun `login should throw LOGIN_REQUIRED when user not found`() {
        assertFailsWith<Exception> {
            authService.login(1L)
        }

        verify(exactly = 1) { userProviderService.getUser(1L) }
    }
}
