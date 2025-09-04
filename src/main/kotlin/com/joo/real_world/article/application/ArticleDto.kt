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

data class GetArticleQuery(
    val tag: String?,
    val author: String?,
    val favorited: Boolean = false,
    val limit: Long = 20,
    val offset: Long = 0
)

data class CreateArticleCommand(
    val title: String,
    val description: String,
    val body: String,
    val tagList: List<String>? = null
)

data class UpdateArticleCommand(
    val slug: String,
    val title: String? = null,
    val description: String? = null,
    val body: String? = null
)

data class AddCommentCommand(
    val slug: String,
    val body: String
)

data class UpdateCommentCommand(
    val slug: String,
    val body: String
)