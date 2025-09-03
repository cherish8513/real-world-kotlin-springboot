package com.joo.real_world.article.application

import com.joo.real_world.security.infrastructure.UserSession

interface ViewArticleUseCase {
    fun getArticle(slug: String, userSession: UserSession): ArticleDto
    fun getArticles(getArticleQuery: GetArticleQuery, userSession: UserSession): List<ArticleDto>
}