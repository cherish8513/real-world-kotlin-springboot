package com.joo.real_world.article.infrastructure

import jakarta.persistence.*
import org.springframework.data.annotation.CreatedDate
import org.springframework.data.annotation.LastModifiedDate
import org.springframework.data.jpa.domain.support.AuditingEntityListener
import java.time.LocalDateTime

@EntityListeners(AuditingEntityListener::class)
@Entity
@Table(name = "article")
class ArticleEntity(
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long? = null,
    @Column(unique = true)
    val slug: String,
    val title: String,
    val description: String,
    val body: String,
    @OneToMany(mappedBy = "article", cascade = [CascadeType.ALL], orphanRemoval = true)
    val articleTags: MutableList<ArticleTagEntity> = mutableListOf(),
    @OneToMany(mappedBy = "article", cascade = [CascadeType.ALL], orphanRemoval = true)
    val comments: MutableList<CommentEntity> = mutableListOf(),
    @OneToMany(mappedBy = "article", cascade = [CascadeType.ALL], orphanRemoval = true)
    val favorites: MutableList<FavoriteEntity> = mutableListOf(),
    val authorId: Long
) {
    @CreatedDate
    @Column(updatable = false)
    lateinit var createdAt: LocalDateTime

    @LastModifiedDate
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