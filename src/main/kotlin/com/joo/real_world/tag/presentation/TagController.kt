package com.joo.real_world.tag.presentation

import com.joo.real_world.common.config.ApiController
import com.joo.real_world.tag.application.usecase.TagQueryUseCase
import com.joo.real_world.tag.presentation.response.MultipleTagResponse
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping

@ApiController
@RequestMapping("/tags")
class TagController(
    private val tagQueryUseCase: TagQueryUseCase
) {
    @GetMapping
    fun getTags(): MultipleTagResponse {
        return MultipleTagResponse(tagQueryUseCase.getTags())
    }
}