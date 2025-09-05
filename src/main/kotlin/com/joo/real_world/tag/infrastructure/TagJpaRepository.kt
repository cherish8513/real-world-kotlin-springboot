package com.joo.real_world.tag.infrastructure

import com.joo.real_world.common.util.assertNotNull
import com.joo.real_world.tag.domain.Tag
import com.joo.real_world.tag.domain.TagRepository
import com.joo.real_world.tag.domain.vo.TagId
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

interface ITagJpaRepository : JpaRepository<TagEntity, Long> {
    fun findByNameIn(names: Collection<String>): List<TagEntity>
}

@Repository
class TagJpaRepository(private val tagJpaRepository: ITagJpaRepository) : TagRepository {
    override fun findOrCreateTags(tagNames: List<String>): List<Tag> {
        if (tagNames.isEmpty()) return emptyList()

        val existingTags = tagJpaRepository.findByNameIn(tagNames)
        val existingTagMap = existingTags.associateBy { it.name }

        val newTags = tagNames
            .filterNot { existingTagMap.containsKey(it) }
            .map { TagEntity(name = it) }

        val savedTags = if (newTags.isNotEmpty()) tagJpaRepository.saveAll(newTags) else emptyList()

        return (existingTags + savedTags).map {
            Tag(
                id = TagId(it.id.assertNotNull()),
                name = it.name
            )
        }
    }

    override fun findAll(): List<Tag> {
        return tagJpaRepository.findAll().map { Tag(id = TagId(it.id.assertNotNull()), it.name )}
    }
}
