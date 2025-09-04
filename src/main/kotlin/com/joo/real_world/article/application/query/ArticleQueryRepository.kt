package com.joo.real_world.article.application.query

import com.joo.real_world.article.application.ArticleDto
import com.joo.real_world.article.application.CommentDto
import com.joo.real_world.article.application.query.dto.ArticleCondition
import com.joo.real_world.common.application.query.PageSpec

interface ArticleQueryRepository {
    fun findBySlugAndUserId(slug: String, userId: Long): ArticleDto
    fun findByCondition(articleCondition: ArticleCondition, pageSpec: PageSpec): List<ArticleDto>
    fun findFeed(articleCondition: ArticleCondition, pageSpec: PageSpec): List<ArticleDto>
    fun findComments(slug: String, userId: Long): List<CommentDto>
}