package com.joo.real_world.article.infrastructure

import jakarta.persistence.CascadeType
import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.OneToMany
import jakarta.persistence.Table
import java.time.LocalDateTime

@Entity
@Table(name = "article")
class ArticleEntity(
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long? = null,
    val slug: String,
    val title: String,
    val description: String,
    val body: String,
    @OneToMany(mappedBy = "article", cascade = [CascadeType.ALL], orphanRemoval = true)
    val articleTags: MutableList<ArticleTagEntity> = mutableListOf(),
    val authorId: Long
) {
    @Column(insertable = false, updatable = false)
    lateinit var createdAt: LocalDateTime

    @Column(insertable = false, updatable = false)
    lateinit var updatedAt: LocalDateTime

    fun addTag(tag: TagEntity) {
        val articleTag = ArticleTagEntity(article = this, tag = tag)
        articleTags.add(articleTag)
        tag.articleTags.add(articleTag)
    }

    fun removeTag(tag: TagEntity) {
        val articleTag = articleTags.find { it.tag == tag }
        articleTag?.let {
            articleTags.remove(it)
            tag.articleTags.remove(it)
        }
    }
}