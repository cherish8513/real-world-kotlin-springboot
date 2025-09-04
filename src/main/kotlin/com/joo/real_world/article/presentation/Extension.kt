package com.joo.real_world.article.presentation

import com.joo.real_world.article.application.ArticleDto
import com.joo.real_world.article.application.CommentDto
import com.joo.real_world.article.presentation.response.*

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

fun CommentDto.toResponse(): CommentResponse {
    return CommentResponse(
        comment = CommentResponseDto(
            id = commentId,
            body = body,
            createdAt = createdAt,
            updatedAt = updatedAt,
            author = AuthorResponseDto(
                username = author.username,
                bio = author.bio,
                image = author.image,
                following = author.following
            )
        )
    )
}

fun List<CommentDto>.toResponse(): MultipleCommentResponse {
    return MultipleCommentResponse(
        comments = this.map {
            CommentResponseDto(
                id = it.commentId,
                body = it.body,
                createdAt = it.createdAt,
                updatedAt = it.updatedAt,
                author = AuthorResponseDto(
                    username = it.author.username,
                    bio = it.author.bio,
                    image = it.author.image,
                    following = it.author.following
                )
            )
        }
    )
}