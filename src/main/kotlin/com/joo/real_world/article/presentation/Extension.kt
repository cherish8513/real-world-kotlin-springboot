package com.joo.real_world.article.presentation

import com.joo.real_world.article.application.ArticleDto
import com.joo.real_world.article.presentation.response.ArticleResponse
import com.joo.real_world.article.presentation.response.ArticleResponseDto
import com.joo.real_world.article.presentation.response.AuthorResponseDto

fun ArticleDto.toResponse(): ArticleResponse {
    return ArticleResponse(
        article = ArticleResponseDto(
            slug = this.slug,
            title = this.title,
            description = this.description,
            body = this.body,
            tagList = this.tagList ?: mutableListOf(),
            createdAt = this.createdAt,
            updatedAt = this.updatedAt,
            favorited = this.favorited,
            favoritesCount = this.favoritesCount,
            author = AuthorResponseDto(
                username = this.author.username,
                bio = this.author.bio,
                image = this.author.image,
                following = this.author.following
            )
        )
    )
}