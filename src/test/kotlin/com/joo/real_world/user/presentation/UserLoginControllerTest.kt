package com.joo.real_world.user.presentation

import com.joo.real_world.AbstractControllerTest
import com.joo.real_world.user.application.UserDto
import com.joo.real_world.user.application.usecase.LoginUseCase
import com.joo.real_world.user.application.usecase.UserCommandUseCase
import com.joo.real_world.user.presentation.request.LoginRequest
import com.joo.real_world.user.presentation.request.LoginUser
import com.joo.real_world.user.presentation.request.RegisterRequest
import com.joo.real_world.user.presentation.request.RegisterUser
import com.ninjasquad.springmockk.MockkBean
import io.mockk.every
import io.mockk.verify
import org.junit.jupiter.api.Test
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.http.MediaType
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status

@WebMvcTest(UserLoginController::class)
class UserLoginControllerTest : AbstractControllerTest() {
    @MockkBean
    private lateinit var loginUseCase: LoginUseCase

    @MockkBean
    private lateinit var userCommandUseCase: UserCommandUseCase

    private val testUsername = "testuser"
    private val testEmail = "test@example.com"
    private val testPassword = "password123"
    private val testToken = "dummy-jwt-token"
    private val testUserId = 1L
    private val testUserDto = UserDto(
        id = testUserId,
        username = testUsername,
        email = testEmail,
        bio = null,
        image = null,
    )

    @Test
    fun `should register user and return response when request is valid`() {
        // given
        val registerRequest = RegisterRequest(
            registerUser = RegisterUser(
                username = testUsername,
                email = testEmail,
                password = testPassword
            )
        )

        every { userCommandUseCase.register(testUsername, testEmail, testPassword) } returns testUserDto

        val requestJson = objectMapper.writeValueAsString(registerRequest)

        // when & then
        mockMvc.perform(
            post("/api/users")
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestJson)
        )
            .andExpect(status().isOk)
            .andExpect(jsonPath("$.user.username").value(testUsername))
            .andExpect(jsonPath("$.user.email").value(testEmail))

        verify(exactly = 1) { userCommandUseCase.register(testUsername, testEmail, testPassword) }
    }

    @Test
    fun `should login user and return token when credentials are valid`() {
        // given
        val loginRequest = LoginRequest(
            loginUser = LoginUser(
                email = testEmail,
                password = testPassword
            )
        )

        every { loginUseCase.getUser(email = testEmail, password = testPassword) } returns testUserDto
        every { authService.login(testUserId) } returns testToken

        val requestJson = objectMapper.writeValueAsString(loginRequest)

        // when & then
        mockMvc.perform(
            post("/api/users/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestJson)
        )
            .andExpect(status().isOk)
            .andExpect(jsonPath("$.user.username").value(testUsername))
            .andExpect(jsonPath("$.user.email").value(testEmail))
            .andExpect(jsonPath("$.user.token").value(testToken))

        verify(exactly = 1) { loginUseCase.getUser(email = testEmail, password = testPassword) }
        verify(exactly = 1) { authService.login(testUserId) }
    }

    @Test
    fun `should return bad request when register request is invalid`() {
        // given
        val invalidRequest = RegisterRequest(
            registerUser = RegisterUser(
                username = "",
                email = "invalid-email",
                password = ""
            )
        )

        val requestJson = objectMapper.writeValueAsString(invalidRequest)

        // when & then
        mockMvc.perform(
            post("/api/users")
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestJson)
        )
            .andExpect(status().isBadRequest)
            .andExpect(jsonPath("$.errors.body").isArray)

        verify(exactly = 0) { userCommandUseCase.register(any(), any(), any()) }
    }

    @Test
    fun `should return bad request when login request is invalid`() {
        // given
        val invalidLogin = LoginRequest(
            loginUser = LoginUser(
                email = "invalid-email",
                password = ""
            )
        )

        val requestJson = objectMapper.writeValueAsString(invalidLogin)

        // when & then
        mockMvc.perform(
            post("/api/users/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestJson)
        )
            .andExpect(status().isBadRequest)
            .andExpect(jsonPath("$.errors.body").isArray)

        verify(exactly = 0) { loginUseCase.getUser(any(), any()) }
        verify(exactly = 0) { authService.login(any()) }
    }
}
