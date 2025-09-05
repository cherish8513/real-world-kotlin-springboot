package com.joo.real_world.tag.domain

interface TagRepository {
    fun findOrCreateTags(tagNames: List<String>): List<Tag>
    fun findAll(): List<Tag>
}