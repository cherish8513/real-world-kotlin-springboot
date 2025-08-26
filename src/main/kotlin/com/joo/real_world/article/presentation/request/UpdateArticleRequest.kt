package com.joo.real_world.article.presentation.request

import com.fasterxml.jackson.annotation.JsonProperty

data class UpdateArticleRequest(
    @field:JsonProperty("article")
    val updateArticleRequestDto: UpdateArticleRequestDto
)


data class UpdateArticleRequestDto(
    val title: String? = null,
    val description: String? = null,
    val body: String? = null
)