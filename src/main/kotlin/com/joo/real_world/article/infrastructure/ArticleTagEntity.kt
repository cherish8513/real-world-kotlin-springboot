package com.joo.real_world.article.infrastructure

import com.joo.real_world.tag.infrastructure.TagEntity
import jakarta.persistence.*

@Entity
@Table(name = "article_tag")
class ArticleTagEntity(
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long? = null,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "articleId")
    val article: ArticleEntity,

    val tagId: Long
)