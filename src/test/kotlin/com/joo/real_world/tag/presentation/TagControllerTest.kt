package com.joo.real_world.tag.presentation

import com.joo.real_world.AbstractControllerTest
import com.joo.real_world.tag.application.usecase.TagQueryUseCase
import com.joo.real_world.tag.presentation.response.MultipleTagResponse
import com.ninjasquad.springmockk.MockkBean
import io.mockk.every
import io.mockk.verify
import org.junit.jupiter.api.Test
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.http.MediaType
import org.springframework.security.test.context.support.WithMockUser
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.content
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status

@WebMvcTest(TagController::class)
class TagControllerTest : AbstractControllerTest() {
    @MockkBean
    private lateinit var tagQueryUseCase: TagQueryUseCase

    @Test
    @WithMockUser
    fun `getTags should return all tags`() {
        // given
        val tags = listOf("kotlin", "spring", "ddd")
        every { tagQueryUseCase.getTags() } returns tags
        val expectedResponse = MultipleTagResponse(tags)

        // when & then
        mockMvc.perform(get("/api/tags"))
            .andExpect(status().isOk)
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(content().json(objectMapper.writeValueAsString(expectedResponse)))

        verify(exactly = 1) { tagQueryUseCase.getTags() }
    }
}
