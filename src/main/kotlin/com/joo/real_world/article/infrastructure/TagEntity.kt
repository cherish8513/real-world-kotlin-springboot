package com.joo.real_world.article.infrastructure

import jakarta.persistence.*

@Entity
@Table(name = "tag")
class TagEntity(
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long? = null,

    @Column(unique = true)
    val name: String,

    @OneToMany(mappedBy = "tag", cascade = [CascadeType.ALL], orphanRemoval = true)
    val articleTags: MutableList<ArticleTagEntity> = mutableListOf()
)