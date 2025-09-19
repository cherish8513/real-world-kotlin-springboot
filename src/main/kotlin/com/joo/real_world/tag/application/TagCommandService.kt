package com.joo.real_world.tag.application

import com.joo.real_world.tag.domain.vo.TagId

interface TagCommandService {
    fun findOrCreateTags(tagNames: List<String>): List<TagId>
}