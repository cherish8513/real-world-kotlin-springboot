package com.joo.real_world.user.presentation

import com.joo.real_world.AbstractControllerTest
import com.joo.real_world.user.application.ProfileDto
import com.joo.real_world.user.application.usecase.FollowManagementUseCase
import com.joo.real_world.user.application.usecase.ViewProfileUseCase
import com.ninjasquad.springmockk.MockkBean
import io.mockk.every
import org.junit.jupiter.api.Test
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.security.test.context.support.WithMockUser
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status

@WebMvcTest(ProfileController::class)
class ProfileControllerTest : AbstractControllerTest() {

    @MockkBean
    private lateinit var viewProfileUseCase: ViewProfileUseCase

    @MockkBean
    private lateinit var followManagementUseCase: FollowManagementUseCase

    @Test
    @WithMockUser
    fun `success to get profile`() {
        // given
        val username = "john_doe"
        val bio = "Hello, world!"
        val image = "https://example.com/avatar.png"
        val profileDto = ProfileDto(
            username = "john_doe",
            bio = "Hello, world!",
            image = "https://example.com/avatar.png",
            following = true
        )
        every { viewProfileUseCase.getProfile(username, testUserSession.userId) } returns profileDto

        // when & then
        mockMvc.perform(get("/api/profiles/{username}", username))
            .andExpect(status().isOk)
            .andExpect(jsonPath("$.profile.username").value(username))
            .andExpect(jsonPath("$.profile.bio").value(bio))
            .andExpect(jsonPath("$.profile.image").value(image))
            .andExpect(jsonPath("$.profile.following").value(true))
    }

    @Test
    @WithMockUser
    fun `success to follow`() {
        // given
        val username = "john_doe"
        val bio = "Hello, world!"
        val image = "https://example.com/avatar.png"
        val profileDto = ProfileDto(
            username = "john_doe",
            bio = "Hello, world!",
            image = "https://example.com/avatar.png",
            following = true
        )
        every { followManagementUseCase.follow(testUserSession.userId, username) } returns profileDto

        // when & then
        mockMvc.perform(post("/api/profiles/{username}/follow", username))
            .andExpect(status().isOk)
            .andExpect(jsonPath("$.profile.username").value(username))
            .andExpect(jsonPath("$.profile.bio").value(bio))
            .andExpect(jsonPath("$.profile.image").value(image))
            .andExpect(jsonPath("$.profile.following").value(true))
    }

    @Test
    @WithMockUser
    fun `success to unfollow`() {
        // given
        val username = "john_doe"
        val bio = "Hello, world!"
        val image = "https://example.com/avatar.png"
        val profileDto = ProfileDto(
            username = "john_doe",
            bio = "Hello, world!",
            image = "https://example.com/avatar.png",
            following = false
        )
        every { followManagementUseCase.unfollow(testUserSession.userId, username) } returns profileDto

        // when & then
        mockMvc.perform(delete("/api/profiles/{username}/follow", username))
            .andExpect(status().isOk)
            .andExpect(jsonPath("$.profile.username").value(username))
            .andExpect(jsonPath("$.profile.bio").value(bio))
            .andExpect(jsonPath("$.profile.image").value(image))
            .andExpect(jsonPath("$.profile.following").value(false))
    }
}
