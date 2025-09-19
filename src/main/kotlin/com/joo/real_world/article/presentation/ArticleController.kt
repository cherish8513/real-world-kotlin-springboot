package com.joo.real_world.article.presentation

import com.joo.real_world.article.application.AddCommentCommand
import com.joo.real_world.article.application.CreateArticleCommand
import com.joo.real_world.article.application.GetArticleQuery
import com.joo.real_world.article.application.UpdateArticleCommand
import com.joo.real_world.article.application.usecase.ArticleCommandUseCase
import com.joo.real_world.article.application.usecase.ArticleQueryUseCase
import com.joo.real_world.article.presentation.request.AddCommentRequest
import com.joo.real_world.article.presentation.request.CreateArticleRequest
import com.joo.real_world.article.presentation.request.GetArticleWithFilterRequest
import com.joo.real_world.article.presentation.request.UpdateArticleRequest
import com.joo.real_world.article.presentation.response.ArticleResponse
import com.joo.real_world.article.presentation.response.CommentResponse
import com.joo.real_world.article.presentation.response.MultipleCommentResponse
import com.joo.real_world.common.config.ApiController
import com.joo.real_world.security.infrastructure.UserSession
import org.springframework.security.core.annotation.AuthenticationPrincipal
import org.springframework.web.bind.annotation.*

@ApiController
@RequestMapping("/articles")
class ArticleController(
    private val articleQueryUseCase: ArticleQueryUseCase,
    private val articleCommandUseCase: ArticleCommandUseCase
) {
    @GetMapping("/{slug}")
    fun getArticle(@PathVariable slug: String, @AuthenticationPrincipal userSession: UserSession): ArticleResponse {
        return articleQueryUseCase.getArticle(slug, userSession).toResponse()
    }

    @GetMapping
    fun getArticles(
        @ModelAttribute getArticleWithFilterRequest: GetArticleWithFilterRequest,
        @AuthenticationPrincipal userSession: UserSession
    ): List<ArticleResponse> {
        return articleQueryUseCase.getArticles(
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

    @GetMapping("/feed")
    fun getFeedArticles(
        @ModelAttribute getArticleWithFilterRequest: GetArticleWithFilterRequest,
        @AuthenticationPrincipal userSession: UserSession
    ): List<ArticleResponse> {
        return articleQueryUseCase.getFeedArticles(
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
        val articleId = articleCommandUseCase.createArticle(
            CreateArticleCommand(
                title = createArticleRequest.createArticleRequestDto.title,
                description = createArticleRequest.createArticleRequestDto.description,
                body = createArticleRequest.createArticleRequestDto.body,
                tagList = createArticleRequest.createArticleRequestDto.tagList
            ), userSession
        )

        return articleQueryUseCase.getArticle(articleId = articleId, userSession).toResponse()
    }

    @PutMapping("/{slug}")
    fun updateArticle(
        @RequestBody updateArticleRequest: UpdateArticleRequest,
        @PathVariable slug: String,
        @AuthenticationPrincipal userSession: UserSession
    ): ArticleResponse {
        val articleId = articleCommandUseCase.updateArticle(
            UpdateArticleCommand(
                slug = slug,
                title = updateArticleRequest.updateArticleRequestDto.title,
                description = updateArticleRequest.updateArticleRequestDto.description,
                body = updateArticleRequest.updateArticleRequestDto.body
            ), userSession
        )

        return articleQueryUseCase.getArticle(articleId = articleId, userSession).toResponse()
    }

    @DeleteMapping("/{slug}")
    fun deleteArticle(
        @PathVariable slug: String,
        @AuthenticationPrincipal userSession: UserSession
    ) {
        return articleCommandUseCase.deleteArticle(slug, userSession)
    }

    @PostMapping("/{slug}/comments")
    fun addComment(
        @RequestBody addCommentRequest: AddCommentRequest,
        @PathVariable slug: String,
        @AuthenticationPrincipal userSession: UserSession
    ): CommentResponse {
        val commentId = articleCommandUseCase.addComment(AddCommentCommand(slug = slug, body = addCommentRequest.addCommentRequestDto.body), userSession)

        return articleQueryUseCase.getComment(commentId = commentId, userSession = userSession).toResponse()
    }

    @PostMapping("/{slug}/comments/{id}")
    fun deleteComment(
        @PathVariable slug: String,
        @PathVariable id: Long,
        @AuthenticationPrincipal userSession: UserSession
    ) {
        return articleCommandUseCase.deleteComment(slug = slug, commentId = id, userSession)
    }

    @GetMapping("/{slug}/comments")
    fun getComments(
        @PathVariable slug: String,
        @AuthenticationPrincipal userSession: UserSession
    ): MultipleCommentResponse {
        return articleQueryUseCase.getComments(slug, userSession).toResponse()
    }

    @PostMapping("/{slug}/favorite")
    fun favoriteArticle(
        @PathVariable slug: String,
        @AuthenticationPrincipal userSession: UserSession
    ): ArticleResponse {
        val articleId = articleCommandUseCase.favoriteArticle(slug, userSession)
        return articleQueryUseCase.getArticle(articleId = articleId, userSession).toResponse()
    }

    @DeleteMapping("/{slug}/favorite")
    fun unFavoriteArticle(
        @PathVariable slug: String,
        @AuthenticationPrincipal userSession: UserSession
    ): ArticleResponse {
        val articleId = articleCommandUseCase.unFavoriteArticle(slug, userSession)
        return articleQueryUseCase.getArticle(articleId = articleId, userSession).toResponse()
    }
}