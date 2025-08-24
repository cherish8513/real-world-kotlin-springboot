package com.joo.real_world.article.infrastructure

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

interface IArticleJpaRepository: JpaRepository<ArticleEntity, Long>

@Repository
class ArticleJpaRepository(
    private val articleJpaRepository: IArticleJpaRepository
) {
}