package com.joo.real_world.tag.presentation.response

import com.fasterxml.jackson.annotation.JsonProperty

data class MultipleTagResponse (
    @field:JsonProperty("tags")
    val tags: List<String>
)