package com.joo.real_world.article.presentation.request

data class GetArticleWithFilterRequest(
    val tag: String?,
    val author: String?,
    val favorited: Boolean = false,
    val limit: Long = 20,
    val offset: Long = 0
)