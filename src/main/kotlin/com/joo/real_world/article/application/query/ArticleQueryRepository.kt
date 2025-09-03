package com.joo.real_world.article.application.query

import com.joo.real_world.article.application.ArticleDto
import com.joo.real_world.article.application.query.dto.ArticleCondition
import com.joo.real_world.common.application.query.PageSpec

interface ArticleQueryRepository {
    fun findByCondition(articleCondition: ArticleCondition, pageSpec: PageSpec): List<ArticleDto>
}