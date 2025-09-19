package com.joo.real_world.tag.application

import com.joo.real_world.common.util.assertNotNull
import com.joo.real_world.tag.domain.TagRepository
import com.joo.real_world.tag.domain.vo.TagId
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Transactional(rollbackFor = [Exception::class])
@Service
class TagCommandServiceImpl(
    private val tagRepository: TagRepository
) : TagCommandService {
    override fun findOrCreateTags(tagNames: List<String>): List<TagId> {
        return tagRepository.findOrCreateTags(tagNames).map { it.id.assertNotNull() }
    }
}