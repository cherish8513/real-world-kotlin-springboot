package com.joo.real_world.article.application.query.dto

data class ArticleCondition(
    val userId: Long,
    val tag: String? = null,
    val authorId: Long? = null,
    val favoriteByUserId: Long? = null,
)