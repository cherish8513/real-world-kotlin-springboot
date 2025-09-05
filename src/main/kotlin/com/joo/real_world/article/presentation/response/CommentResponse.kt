package com.joo.real_world.article.presentation.response

import com.fasterxml.jackson.annotation.JsonProperty

data class CommentResponse (
    @field:JsonProperty("comment")
    val comment: CommentResponseDto
)

data class MultipleCommentResponse (
    @field:JsonProperty("comments")
    val comments: List<CommentResponseDto>
)
data class CommentResponseDto(
    val id: Long,
    val body: String,
    val createdAt: String,
    val updatedAt: String,
    @field:JsonProperty("author")
    val author: AuthorResponseDto
)