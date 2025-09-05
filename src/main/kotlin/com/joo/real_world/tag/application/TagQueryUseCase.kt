package com.joo.real_world.tag.application

interface TagQueryUseCase {
    fun getTags(): List<String>
}