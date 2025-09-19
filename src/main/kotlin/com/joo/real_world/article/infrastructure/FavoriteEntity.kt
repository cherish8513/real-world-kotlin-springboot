package com.joo.real_world.article.infrastructure

import jakarta.persistence.*

@Entity
@Table(name = "favorite")
class FavoriteEntity (
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long? = null,
    val userId: Long,
    val articleId: Long
) {
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "articleId", insertable = false, updatable = false)
    lateinit var article: ArticleEntity
}