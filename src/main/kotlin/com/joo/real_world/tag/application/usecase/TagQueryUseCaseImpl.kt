package com.joo.real_world.tag.application.usecase

import com.joo.real_world.tag.domain.TagRepository
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Transactional(rollbackFor = [Exception::class])
@Service
class TagQueryUseCaseImpl(
    private val tagRepository: TagRepository
): TagQueryUseCase {
    override fun getTags(): List<String> {
        return tagRepository.findAll().map { it.name }
    }
}