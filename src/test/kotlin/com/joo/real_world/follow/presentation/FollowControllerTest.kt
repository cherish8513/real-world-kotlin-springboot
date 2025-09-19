package com.joo.real_world.follow.presentation

import com.joo.real_world.AbstractControllerTest
import com.joo.real_world.follow.application.usecase.FollowCommandUseCase
import com.joo.real_world.user.application.ProfileDto
import com.ninjasquad.springmockk.MockkBean
import io.mockk.every
import org.junit.jupiter.api.Test
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.security.test.context.support.WithMockUser
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status

@WebMvcTest(FollowController::class)
class FollowControllerTest : AbstractControllerTest() {
    @MockkBean
    private lateinit var followCommandUseCase: FollowCommandUseCase

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
        every { followCommandUseCase.follow(testUserSession.userId, username) } returns profileDto

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
        every { followCommandUseCase.unfollow(testUserSession.userId, username) } returns profileDto

        // when & then
        mockMvc.perform(delete("/api/profiles/{username}/follow", username))
            .andExpect(status().isOk)
            .andExpect(jsonPath("$.profile.username").value(username))
            .andExpect(jsonPath("$.profile.bio").value(bio))
            .andExpect(jsonPath("$.profile.image").value(image))
            .andExpect(jsonPath("$.profile.following").value(false))
    }
}
