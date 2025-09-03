package com.joo.real_world.article.presentation

import com.joo.real_world.article.application.*
import com.joo.real_world.article.presentation.request.CreateArticleRequest
import com.joo.real_world.article.presentation.request.GetArticleWithFilterRequest
import com.joo.real_world.article.presentation.request.UpdateArticleRequest
import com.joo.real_world.article.presentation.response.ArticleResponse
import com.joo.real_world.common.config.ApiController
import com.joo.real_world.security.infrastructure.UserSession
import org.springframework.data.repository.query.Param
import org.springframework.security.core.annotation.AuthenticationPrincipal
import org.springframework.web.bind.annotation.*

@ApiController
@RequestMapping("/articles")
class ArticleController(
    private val viewArticleUseCase: ViewArticleUseCase,
    private val managementArticleUseCase: ManagementArticleUseCase
) {
    @GetMapping("/{slug}")
    fun getArticle(@PathVariable slug: String, @AuthenticationPrincipal userSession: UserSession): ArticleResponse {
        return viewArticleUseCase.getArticle(slug, userSession).toResponse()
    }

    @GetMapping
    fun getArticles(
        @ModelAttribute getArticleWithFilterRequest: GetArticleWithFilterRequest,
        @AuthenticationPrincipal userSession: UserSession
    ): List<ArticleResponse> {
        return viewArticleUseCase.getArticles(
            GetArticleQuery(
                tag = getArticleWithFilterRequest.tag,
                author = getArticleWithFilterRequest.author,
                favorited = getArticleWithFilterRequest.favorited,
                limit = getArticleWithFilterRequest.limit,
                offset = getArticleWithFilterRequest.offset,
            ),
            userSession
        ).map { it.toResponse() }
    }

    @PostMapping
    fun createArticle(
        @RequestBody createArticleRequest: CreateArticleRequest,
        @AuthenticationPrincipal userSession: UserSession
    ): ArticleResponse {
        return managementArticleUseCase.createArticle(
            CreateArticleCommand(
                title = createArticleRequest.createArticleRequestDto.title,
                description = createArticleRequest.createArticleRequestDto.description,
                body = createArticleRequest.createArticleRequestDto.body,
                tagList = createArticleRequest.createArticleRequestDto.tagList
            ), userSession
        ).toResponse()
    }

    @PutMapping("/{slug}")
    fun updateArticle(
        @RequestBody updateArticleRequest: UpdateArticleRequest,
        @PathVariable slug: String,
        @AuthenticationPrincipal userSession: UserSession
    ): ArticleResponse {
        return managementArticleUseCase.updateArticle(
            UpdateArticleCommand(
                title = updateArticleRequest.updateArticleRequestDto.title,
                description = updateArticleRequest.updateArticleRequestDto.description,
                body = updateArticleRequest.updateArticleRequestDto.body
            ), slug, userSession
        ).toResponse()
    }

    @DeleteMapping("/{slug}")
    fun deleteArticle(
        @PathVariable slug: String,
        @AuthenticationPrincipal userSession: UserSession
    ) {
        return managementArticleUseCase.deleteArticle(slug, userSession)
    }
}