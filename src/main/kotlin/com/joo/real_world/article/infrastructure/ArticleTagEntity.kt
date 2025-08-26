package com.joo.real_world.article.infrastructure

import jakarta.persistence.*

@Entity
@Table(name = "article_tag")
class ArticleTagEntity(
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long? = null,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "article_id")
    val article: ArticleEntity,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "tag_id")
    val tag: TagEntity
)