package com.joo.real_world.tag.application.usecase

interface TagQueryUseCase {
    fun getTags(): List<String>
}