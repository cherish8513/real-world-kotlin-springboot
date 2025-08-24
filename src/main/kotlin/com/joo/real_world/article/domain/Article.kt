package com.joo.real_world.article.domain

import com.joo.real_world.article.domain.vo.ArticleId
import com.joo.real_world.article.domain.vo.Body
import com.joo.real_world.article.domain.vo.Description
import com.joo.real_world.article.domain.vo.Slug
import com.joo.real_world.article.domain.vo.Tag
import com.joo.real_world.article.domain.vo.Title
import com.joo.real_world.user.domain.vo.UserId
import java.time.LocalDateTime

class Article(
    val id: ArticleId? = null,
    val slug: Slug,
    val title: Title,
    val description: Description,
    val body: Body,
    val tags: List<Tag>,
    val authorId: UserId,
    val createdAt: LocalDateTime = LocalDateTime.now(),
    val updatedAt: LocalDateTime = LocalDateTime.now()
)