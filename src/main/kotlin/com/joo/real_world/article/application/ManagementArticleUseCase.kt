package com.joo.real_world.article.application

import com.joo.real_world.article.presentation.request.CreateArticleRequest
import com.joo.real_world.article.presentation.request.UpdateArticleRequest
import com.joo.real_world.security.infrastructure.UserSession

interface ManagementArticleUseCase {
    fun createArticle(createArticleRequest: CreateArticleRequest, userSession: UserSession): ArticleDto
    fun updateArticle(updateArticleRequest: UpdateArticleRequest, slug: String, userSession: UserSession): ArticleDto
    fun deleteArticle(slug: String, userSession: UserSession)
}