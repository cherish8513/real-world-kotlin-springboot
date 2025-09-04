package com.joo.real_world.article.application

data class CommentDto (
    val commentId: Long,
    val body: String,
    val createdAt: String,
    val updatedAt: String,
    val author: AuthorDto
)