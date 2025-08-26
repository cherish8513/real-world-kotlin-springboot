package com.joo.real_world.article.application

data class ArticleDto(
    val slug: String,
    val title: String,
    val description: String,
    val body: String,
    val tagList: List<String>?,
    val createdAt: String,
    val updatedAt: String,
    val favorited: Boolean,
    val favoritesCount: Int,
    val author: AuthorDto
)

data class AuthorDto(
    val username: String,
    val bio: String?,
    val image: String?,
    val following: Boolean
)
