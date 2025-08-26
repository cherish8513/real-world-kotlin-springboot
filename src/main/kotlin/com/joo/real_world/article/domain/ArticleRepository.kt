package com.joo.real_world.article.domain

import com.joo.real_world.article.domain.vo.ArticleId
import com.joo.real_world.article.domain.vo.Slug

interface ArticleRepository {
    fun save(article: Article): Article
    fun findBySlug(slug: Slug): Article?
    fun delete(articleId: ArticleId)
}