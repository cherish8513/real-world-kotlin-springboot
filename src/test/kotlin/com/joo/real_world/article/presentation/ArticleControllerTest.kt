package com.joo.real_world.article.presentation

import com.joo.real_world.AbstractControllerTest
import com.joo.real_world.article.application.ArticleDto
import com.joo.real_world.article.application.AuthorDto
import com.joo.real_world.article.application.CommentDto
import com.joo.real_world.article.application.usecase.ArticleCommandUseCase
import com.joo.real_world.article.application.usecase.ArticleQueryUseCase
import com.joo.real_world.article.presentation.request.*
import com.joo.real_world.common.util.assertNotNull
import com.joo.real_world.security.infrastructure.UserSession
import com.ninjasquad.springmockk.MockkBean
import io.mockk.every
import io.mockk.verify
import org.junit.jupiter.api.Test
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.http.MediaType
import org.springframework.security.test.context.support.WithMockUser
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.content
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status

@WebMvcTest(ArticleController::class)
class ArticleControllerTest : AbstractControllerTest() {

    @MockkBean
    private lateinit var articleQueryUseCase: ArticleQueryUseCase

    @MockkBean
    private lateinit var articleCommandUseCase: ArticleCommandUseCase

    @Test
    @WithMockUser
    fun `getArticle should return article response`() {
        val slug = "test-article"
        val articleDto = ArticleDto(
            slug = slug,
            title = "Title",
            description = "Desc",
            body = "Body",
            tagList = listOf("kotlin", "spring"),
            createdAt = "2025-09-10T15:00:00",
            updatedAt = "2025-09-10T15:00:00",
            favorited = false,
            favoritesCount = 0,
            author = AuthorDto("author", "bio", null, false)
        )

        every { articleQueryUseCase.getArticle(slug, any<UserSession>()) } returns articleDto

        val expectedResponse = articleDto.toResponse()

        mockMvc.perform(get("/api/articles/$slug"))
            .andExpect(status().isOk)
            .andExpect(content().json(objectMapper.writeValueAsString(expectedResponse)))

        verify(exactly = 1) { articleQueryUseCase.getArticle(slug, any<UserSession>()) }
    }

    @Test
    @WithMockUser
    fun `createArticle should return article response`() {
        val request = CreateArticleRequest(
            createArticleRequestDto = CreateArticleRequestDto(
                title = "New Title",
                description = "New Desc",
                body = "New Body",
                tagList = listOf("kotlin")
            )
        )
        val createdArticleId = 1L

        val articleDto = ArticleDto(
            slug = "new-title",
            title = request.createArticleRequestDto.title,
            description = request.createArticleRequestDto.description,
            body = request.createArticleRequestDto.body,
            tagList = request.createArticleRequestDto.tagList,
            createdAt = "2025-09-10T15:00:00",
            updatedAt = "2025-09-10T15:00:00",
            favorited = false,
            favoritesCount = 0,
            author = AuthorDto("author", null, null, false)
        )

        every { articleCommandUseCase.createArticle(any(), any<UserSession>()) } returns createdArticleId
        every { articleQueryUseCase.getArticle(createdArticleId, any<UserSession>()) } returns articleDto

        val expectedResponse = articleDto.toResponse()

        mockMvc.perform(
            post("/api/articles")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request))
        )
            .andExpect(status().isOk)
            .andExpect(content().json(objectMapper.writeValueAsString(expectedResponse)))

        verify(exactly = 1) { articleCommandUseCase.createArticle(any(), any<UserSession>()) }
        verify(exactly = 1) { articleQueryUseCase.getArticle(createdArticleId, any<UserSession>()) }
    }

    @Test
    @WithMockUser
    fun `updateArticle should return article response`() {
        val slug = "test-article"
        val request = UpdateArticleRequest(
            updateArticleRequestDto = UpdateArticleRequestDto(
                title = "Updated Title",
                description = "Updated Desc",
                body = "Updated Body"
            )
        )
        val articleId = 1L

        val articleDto = ArticleDto(
            slug = slug,
            title = request.updateArticleRequestDto.title.assertNotNull(),
            description = request.updateArticleRequestDto.description.assertNotNull(),
            body = request.updateArticleRequestDto.body.assertNotNull(),
            tagList = listOf("kotlin"),
            createdAt = "2025-09-10T15:00:00",
            updatedAt = "2025-09-10T16:00:00",
            favorited = false,
            favoritesCount = 0,
            author = AuthorDto("author", null, null, false)
        )

        every { articleCommandUseCase.updateArticle(any(), any<UserSession>()) } returns articleId
        every { articleQueryUseCase.getArticle(articleId, any<UserSession>()) } returns articleDto

        val expectedResponse = articleDto.toResponse()

        mockMvc.perform(
            put("/api/articles/$slug")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request))
        )
            .andExpect(status().isOk)
            .andExpect(content().json(objectMapper.writeValueAsString(expectedResponse)))

        verify(exactly = 1) { articleCommandUseCase.updateArticle(any(), any<UserSession>()) }
        verify(exactly = 1) { articleQueryUseCase.getArticle(articleId, any<UserSession>()) }
    }

    @Test
    @WithMockUser
    fun `deleteArticle should call delete use case`() {
        val slug = "test-article"
        every { articleCommandUseCase.deleteArticle(slug, any<UserSession>()) } returns Unit

        mockMvc.perform(delete("/api/articles/$slug"))
            .andExpect(status().isOk)

        verify(exactly = 1) { articleCommandUseCase.deleteArticle(slug, any<UserSession>()) }
    }

    @Test
    @WithMockUser
    fun `addComment should return comment response`() {
        val slug = "test-article"
        val request = AddCommentRequest(addCommentRequestDto = AddCommentRequestDto(body = "Test Comment"))
        val commentId = 1L
        val commentDto = CommentDto(
            commentId = commentId,
            body = "Test Comment",
            createdAt = "2025-09-10T15:00:00",
            updatedAt = "2025-09-10T16:00:00",
            author = AuthorDto(
                username = "author",
                bio = null,
                image = null,
                following = false
            )
        )
        val expectedResponse = commentDto.toResponse()

        every { articleCommandUseCase.addComment(any(), any<UserSession>()) } returns commentId
        every { articleQueryUseCase.getComment(commentId, any<UserSession>()) } returns commentDto

        mockMvc.perform(
            post("/api/articles/$slug/comments")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request))
        )
            .andExpect(status().isOk)
            .andExpect(content().json(objectMapper.writeValueAsString(expectedResponse)))

        verify(exactly = 1) { articleCommandUseCase.addComment(any(), any<UserSession>()) }
        verify(exactly = 1) { articleQueryUseCase.getComment(commentId, any<UserSession>()) }
    }

    @Test
    @WithMockUser
    fun `getComments should return list of comments`() {
        val slug = "test-article"
        val commentDtos = listOf(
            CommentDto(
                commentId = 1L,
                body = "Test Comment",
                createdAt = "2025-09-10T15:00:00",
                updatedAt = "2025-09-10T16:00:00",
                author = AuthorDto(
                    username = "author",
                    bio = null,
                    image = null,
                    following = false
                )
            )
        )
        val expectedResponse = commentDtos.toResponse()

        every { articleQueryUseCase.getComments(slug, any<UserSession>()) } returns commentDtos

        mockMvc.perform(get("/api/articles/$slug/comments"))
            .andExpect(status().isOk)
            .andExpect(content().json(objectMapper.writeValueAsString(expectedResponse)))

        verify(exactly = 1) { articleQueryUseCase.getComments(slug, any<UserSession>()) }
    }

    @Test
    @WithMockUser
    fun `favoriteArticle should return article response`() {
        val slug = "test-article"
        val articleId = 1L
        val articleDto = ArticleDto(
            slug,
            "Title",
            "Desc",
            "Body",
            listOf("kotlin"),
            "2025-09-10T15:00:00",
            "2025-09-10T15:00:00",
            true,
            1,
            AuthorDto("author", null, null, false)
        )
        val expectedResponse = articleDto.toResponse()

        every { articleCommandUseCase.favoriteArticle(slug, any<UserSession>()) } returns articleId
        every { articleQueryUseCase.getArticle(articleId, any<UserSession>()) } returns articleDto

        mockMvc.perform(post("/api/articles/$slug/favorite"))
            .andExpect(status().isOk)
            .andExpect(content().json(objectMapper.writeValueAsString(expectedResponse)))

        verify(exactly = 1) { articleCommandUseCase.favoriteArticle(slug, any<UserSession>()) }
        verify(exactly = 1) { articleQueryUseCase.getArticle(articleId, any<UserSession>()) }
    }

    @Test
    @WithMockUser
    fun `unFavoriteArticle should return article response`() {
        val slug = "test-article"
        val articleId = 1L
        val articleDto = ArticleDto(
            slug,
            "Title",
            "Desc",
            "Body",
            listOf("kotlin"),
            "2025-09-10T15:00:00",
            "2025-09-10T15:00:00",
            false,
            0,
            AuthorDto("author", null, null, false)
        )
        val expectedResponse = articleDto.toResponse()

        every { articleCommandUseCase.unFavoriteArticle(slug, any<UserSession>()) } returns articleId
        every { articleQueryUseCase.getArticle(articleId, any<UserSession>()) } returns articleDto

        mockMvc.perform(delete("/api/articles/$slug/favorite"))
            .andExpect(status().isOk)
            .andExpect(content().json(objectMapper.writeValueAsString(expectedResponse)))

        verify(exactly = 1) { articleCommandUseCase.unFavoriteArticle(slug, any<UserSession>()) }
        verify(exactly = 1) { articleQueryUseCase.getArticle(articleId, any<UserSession>()) }
    }
}
