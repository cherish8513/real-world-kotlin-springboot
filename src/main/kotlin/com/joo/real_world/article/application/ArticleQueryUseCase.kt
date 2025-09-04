package com.joo.real_world.article.application

import com.joo.real_world.security.infrastructure.UserSession

interface ArticleQueryUseCase {
    fun getArticle(slug: String, userSession: UserSession): ArticleDto
    fun getArticles(getArticleQuery: GetArticleQuery, userSession: UserSession): List<ArticleDto>
    fun getFeedArticles(getArticleQuery: GetArticleQuery, userSession: UserSession): List<ArticleDto>
    fun getComments(slug: String, userSession: UserSession): List<CommentDto>
}