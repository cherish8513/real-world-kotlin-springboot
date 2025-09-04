package com.joo.real_world.article.application

import com.joo.real_world.security.infrastructure.UserSession

interface ArticleCommandUseCase {
    fun createArticle(createArticleCommand: CreateArticleCommand, userSession: UserSession): ArticleDto
    fun updateArticle(updateArticleCommand: UpdateArticleCommand, userSession: UserSession): ArticleDto
    fun deleteArticle(slug: String, userSession: UserSession)
    fun addComment(addCommentCommand: AddCommentCommand, userSession: UserSession): CommentDto
    fun deleteComment(slug: String, commentId: Long, userSession: UserSession)
}