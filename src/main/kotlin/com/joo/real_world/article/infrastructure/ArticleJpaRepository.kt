package com.joo.real_world.article.infrastructure

import com.joo.real_world.article.domain.Article
import com.joo.real_world.article.domain.ArticleRepository
import com.joo.real_world.article.domain.vo.ArticleId
import com.joo.real_world.article.domain.vo.Slug
import com.joo.real_world.common.util.assertNotNull
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Repository

interface IArticleJpaRepository : JpaRepository<ArticleEntity, Long> {
    fun findBySlug(slug: String): ArticleEntity?
}

@Repository
class ArticleJpaRepository(
    private val articleJpaRepository: IArticleJpaRepository
) : ArticleRepository {
    override fun save(article: Article): Article {
        val baseSlug = article.slug.value
        var newSlug = baseSlug
        var counter = 1
        while (articleJpaRepository.findBySlug(newSlug) != null && counter <= 9) {
            newSlug = "$baseSlug-$counter"
            counter++
        }

        val articleEntity = article.toEntity(slug = newSlug)


        if (article.tags != null) {
            val tags = article.tags.map { TagEntity(name = it.value) }
            tags.map { articleEntity.addTag(it) }
        }

        return articleJpaRepository.save(articleEntity).toDomain()
    }

    override fun findBySlug(slug: Slug): Article? {
        return articleJpaRepository.findBySlug(slug.value)?.toDomain()
    }

    override fun delete(articleId: ArticleId) {
        val article = articleJpaRepository.findByIdOrNull(articleId.value).assertNotNull()
        articleJpaRepository.delete(article)
    }
}