package com.joo.real_world.article.application

import com.joo.real_world.security.infrastructure.UserSession

interface ManagementArticleUseCase {
    fun createArticle(createArticleCommand: CreateArticleCommand, userSession: UserSession): ArticleDto
    fun updateArticle(updateArticleCommand: UpdateArticleCommand, slug: String, userSession: UserSession): ArticleDto
    fun deleteArticle(slug: String, userSession: UserSession)
}