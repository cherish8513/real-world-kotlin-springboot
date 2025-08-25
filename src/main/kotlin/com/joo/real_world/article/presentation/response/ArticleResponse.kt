package com.joo.real_world.article.presentation.response

import com.fasterxml.jackson.annotation.JsonProperty

data class ArticleResponse(
    @field:JsonProperty("article")
    val article: ArticleResponseDto
)

data class ArticleResponseDto(
    val slug: String,
    val title: String,
    val description: String,
    val body: String,
    val tagList: List<String>,
    val createdAt: String,
    val updatedAt: String,
    val favorited: Boolean,
    val favoritesCount: Int,
    @field:JsonProperty("author")
    val author: AuthorResponseDto
)

data class AuthorResponseDto(
    val username: String,
    val bio: String?,
    val image: String?,
    val following: Boolean
)
