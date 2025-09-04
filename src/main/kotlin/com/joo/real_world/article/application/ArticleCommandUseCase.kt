package com.joo.real_world.article.application

import com.joo.real_world.security.infrastructure.UserSession

interface ArticleCommandUseCase {
    fun createArticle(createArticleCommand: CreateArticleCommand, userSession: UserSession): Long
    fun updateArticle(updateArticleCommand: UpdateArticleCommand, userSession: UserSession): Long
    fun deleteArticle(slug: String, userSession: UserSession)
    fun addComment(addCommentCommand: AddCommentCommand, userSession: UserSession): Long
    fun deleteComment(slug: String, commentId: Long, userSession: UserSession)
    fun favoriteArticle(slug: String, userSession: UserSession): Long
    fun unFavoriteArticle(slug: String, userSession: UserSession): Long
}