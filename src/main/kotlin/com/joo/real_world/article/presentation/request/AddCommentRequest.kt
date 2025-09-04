package com.joo.real_world.article.presentation.request

import com.fasterxml.jackson.annotation.JsonProperty

data class AddCommentRequest (
    @field:JsonProperty("comment")
    val addCommentRequestDto: AddCommentRequestDto
)

data class AddCommentRequestDto(
    val body: String,
)