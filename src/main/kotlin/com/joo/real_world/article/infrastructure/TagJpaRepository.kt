package com.joo.real_world.article.infrastructure

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

interface ITagJpaRepository : JpaRepository<TagEntity, Long> {
    fun findByName(name: String): TagEntity?
    fun findByNameIn(names: Collection<String>): List<TagEntity>
}

@Repository
class TagJpaRepository(
    private val tagJpaRepository: ITagJpaRepository
) {
    fun findByName(name: String): TagEntity? =
        tagJpaRepository.findByName(name)

    fun findByNameIn(names: Collection<String>): List<TagEntity> =
        tagJpaRepository.findByNameIn(names)

    fun saveAll(tags: Collection<TagEntity>): List<TagEntity> =
        tagJpaRepository.saveAll(tags)

    fun findOrCreateTags(tagNames: Collection<String>): List<TagEntity> {
        if (tagNames.isEmpty()) return emptyList()

        val existingTags = findByNameIn(tagNames)
        val existingTagMap = existingTags.associateBy { it.name }

        val newTags = tagNames
            .filterNot { existingTagMap.containsKey(it) }
            .map { TagEntity(name = it) }

        val savedTags = if (newTags.isNotEmpty()) saveAll(newTags) else emptyList()

        return existingTags + savedTags
    }
}
