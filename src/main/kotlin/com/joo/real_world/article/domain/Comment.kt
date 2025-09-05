package com.joo.real_world.article.domain

import com.joo.real_world.article.domain.vo.ArticleId
import com.joo.real_world.article.domain.vo.Body
import com.joo.real_world.article.domain.vo.CommentId
import com.joo.real_world.user.domain.vo.UserId
import java.time.LocalDateTime

class Comment(
    val id: CommentId? = null,
    val articleId: ArticleId,
    val authorId: UserId,
    val body: Body,
    val createdAt: LocalDateTime = LocalDateTime.now(),
    val updatedAt: LocalDateTime = LocalDateTime.now()
)