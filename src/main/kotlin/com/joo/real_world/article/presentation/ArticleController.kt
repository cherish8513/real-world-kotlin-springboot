package com.joo.real_world.article.presentation

import com.joo.real_world.article.application.ManagementArticleUseCase
import com.joo.real_world.article.application.ViewArticleUseCase
import com.joo.real_world.article.presentation.request.CreateArticleRequest
import com.joo.real_world.article.presentation.request.UpdateArticleRequest
import com.joo.real_world.article.presentation.response.ArticleResponse
import com.joo.real_world.common.config.ApiController
import com.joo.real_world.security.infrastructure.UserSession
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

    @PostMapping
    fun createArticle(
        @RequestBody createArticleRequest: CreateArticleRequest,
        @AuthenticationPrincipal userSession: UserSession
    ): ArticleResponse {
        return managementArticleUseCase.createArticle(createArticleRequest, userSession).toResponse()
    }

    @PutMapping("/{slug}")
    fun updateArticle(
        @RequestBody updateArticleRequest: UpdateArticleRequest,
        @PathVariable slug: String,
        @AuthenticationPrincipal userSession: UserSession
    ): ArticleResponse {
        return managementArticleUseCase.updateArticle(updateArticleRequest, slug, userSession).toResponse()
    }

    @DeleteMapping("/{slug}")
    fun deleteArticle(
        @PathVariable slug: String,
        @AuthenticationPrincipal userSession: UserSession
    ) {
        return managementArticleUseCase.deleteArticle(slug, userSession)
    }
}