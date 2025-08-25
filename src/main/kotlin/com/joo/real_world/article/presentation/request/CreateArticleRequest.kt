package com.joo.real_world.article.presentation.request

import com.fasterxml.jackson.annotation.JsonProperty

data class CreateArticleRequest (
    @field:JsonProperty("article")
    val createArticleRequestDto: CreateArticleRequestDto
)


data class CreateArticleRequestDto(
    val title: String,
    val description: String,
    val body: String,
    val tagList: List<String>? = null
)