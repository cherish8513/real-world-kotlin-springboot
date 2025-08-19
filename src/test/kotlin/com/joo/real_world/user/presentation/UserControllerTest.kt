package com.joo.real_world.user.presentation

import com.joo.real_world.AbstractControllerTest
import com.joo.real_world.user.application.ModifyUserDto
import com.joo.real_world.user.application.UserDto
import com.joo.real_world.user.application.service.UserService
import com.joo.real_world.user.presentation.request.ModifyUser
import com.joo.real_world.user.presentation.request.ModifyUserRequest
import com.joo.real_world.user.presentation.response.UserResponse
import com.ninjasquad.springmockk.MockkBean
import io.mockk.every
import io.mockk.verify
import org.junit.jupiter.api.Test
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.http.MediaType
import org.springframework.security.test.context.support.WithMockUser
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.*

@WebMvcTest(UserController::class)
class UserControllerTest : AbstractControllerTest() {

    @MockkBean
    private lateinit var userService: UserService

    @Test
    @WithMockUser
    fun `getCurrentUser should return user response`() {
        // given
        val bio = "Test bio"
        val image = "test-image.jpg"
        val userDto = UserDto(
            id = testUserSession.userId,
            username = testUserSession.username,
            email = testUserSession.email,
            bio = bio,
            image = image
        )

        val expectedResponse = UserResponse(
            id = testUserSession.userId,
            username = testUserSession.username,
            email = testUserSession.email,
            bio = bio,
            image = image,
            token = null
        )

        every { userService.getUser(testUserSession.userId) } returns userDto

        // when & then
        mockMvc.perform(get("/api/user"))
            .andExpect(status().isOk)
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(content().json(objectMapper.writeValueAsString(expectedResponse)))

        verify(exactly = 1) { userService.getUser(testUserSession.userId) }
    }

    @Test
    fun `modifyUser should update and return user response`() {
        // given
        val username = "updateduser"
        val email = "updated@example.com"
        val password = "newpassword"
        val bio = "Updated bio"
        val image = "updated-image.jpg"

        val modifyRequest = ModifyUserRequest(
            modifyUser = ModifyUser(
                username = username,
                email = email,
                password = password,
                bio = bio,
                image = image
            )
        )

        val updatedUserDto = UserDto(
            id = testUserSession.userId,
            username = username,
            email = email,
            bio = bio,
            image = image
        )

        val expectedResponse = UserResponse(
            id = testUserSession.userId,
            username = username,
            email = email,
            bio = bio,
            image = image,
            token = null
        )

        val requestJson = objectMapper.writeValueAsString(modifyRequest)

        every { userService.modifyUser(any<ModifyUserDto>()) } returns updatedUserDto


        // when & then
        mockMvc.perform(put("/api/user")
            .contentType(MediaType.APPLICATION_JSON)
            .content(requestJson))
            .andExpect(status().isOk)
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(content().json(objectMapper.writeValueAsString(expectedResponse)))

        verify(exactly = 1) {
            userService.modifyUser(
                match<ModifyUserDto> { dto ->
                    dto.id == testUserSession.userId &&
                            dto.username == username &&
                            dto.email == email &&
                            dto.password == password &&
                            dto.bio == bio &&
                            dto.image == image
                }
            )
        }
    }

    @Test
    @WithMockUser
    fun `should return bad request when modifyUser request is invalid`() {
        // given
        val invalidRequest = ModifyUserRequest(
            modifyUser = ModifyUser(
                username = "",                  // invalid
                email = "invalid-email",        // invalid
                password = null,
                bio = null,
                image = null
            )
        )

        val requestJson = objectMapper.writeValueAsString(invalidRequest)

        // when & then
        mockMvc.perform(
            put("/api/user")
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestJson)
        )
            .andExpect(status().isBadRequest)
            .andExpect(jsonPath("$.errors.body").isArray)
            .andExpect(jsonPath("$.errors.body[?(@ == 'Username must not be blank')]").exists())
            .andExpect(jsonPath("$.errors.body[?(@ == 'Invalid email format')]").exists())

        verify(exactly = 0) { userService.modifyUser(any()) }
    }
}