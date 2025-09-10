package com.joo.real_world.tag.infrastructure

import com.joo.real_world.common.util.assertNotNull
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.transaction.annotation.Transactional

@Transactional
@SpringBootTest
class TagJpaRepositoryTest @Autowired constructor(
    private val tagRepository: TagJpaRepository
) {
    @Test
    @DisplayName("빈 리스트를 입력하면 빈 리스트 반환")
    fun findOrCreateTags_emptyInput() {
        val result = tagRepository.findOrCreateTags(emptyList())
        assertThat(result).isEmpty()
    }

    @Test
    @DisplayName("존재하지 않는 태그는 새로 생성된다")
    fun findOrCreateTags_createNew() {
        val tagNames = listOf("kotlin", "spring")
        val result = tagRepository.findOrCreateTags(tagNames)

        assertThat(result).hasSize(2)
        assertThat(result.map { it.name }).containsExactlyInAnyOrderElementsOf(tagNames)
        assertThat(result.all { it.id.assertNotNull().value > 0 }).isTrue
    }

    @Test
    @DisplayName("이미 존재하는 태그는 중복 생성하지 않고 반환")
    fun findOrCreateTags_existingTags() {
        val tagNames = listOf("kotlin", "spring")
        // 먼저 저장
        val firstSave = tagRepository.findOrCreateTags(tagNames)

        // 다시 저장 시 기존 태그 반환
        val secondSave = tagRepository.findOrCreateTags(tagNames)

        assertThat(secondSave.map { it.id.assertNotNull().value }).containsExactlyInAnyOrderElementsOf(firstSave.map { it.id.assertNotNull().value })
        assertThat(secondSave.map { it.name }).containsExactlyInAnyOrderElementsOf(tagNames)
    }

    @Test
    @DisplayName("findAll은 모든 태그를 반환한다")
    fun findAllTags() {
        val tagNames = listOf("kotlin", "spring", "jpa")
        tagRepository.findOrCreateTags(tagNames)

        val allTags = tagRepository.findAll()
        assertThat(allTags).hasSize(tagNames.size)
        assertThat(allTags.map { it.name }).containsExactlyInAnyOrderElementsOf(tagNames)
    }
}
