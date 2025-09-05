package com.joo.real_world.article.infrastructure

import com.joo.real_world.article.domain.Article
import com.joo.real_world.article.domain.ArticleRepository
import com.joo.real_world.article.domain.vo.ArticleId
import com.joo.real_world.article.domain.vo.Slug
import com.joo.real_world.common.util.assertNotNull
import com.joo.real_world.tag.infrastructure.TagJpaRepository
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Repository

interface IArticleJpaRepository : JpaRepository<ArticleEntity, Long> {
    fun findBySlug(slug: String): ArticleEntity?
}

@Repository
class ArticleJpaRepository(
    private val articleJpaRepository: IArticleJpaRepository,
    private val tagJpaRepository: TagJpaRepository
) : ArticleRepository {
    override fun save(article: Article): ArticleId {
        val articleEntity = article.toEntity()
        articleEntity.articleTags.clear()

        article.tagIds.forEach { tagId ->
            articleEntity.addTag(tagId.value)
        }

        return ArticleId(articleJpaRepository.save(articleEntity).id.assertNotNull())
    }
    override fun findBySlug(slug: Slug): Article? {
        return articleJpaRepository.findBySlug(slug.value)?.toDomain()
    }

    override fun delete(articleId: ArticleId) {
        val article = articleJpaRepository.findByIdOrNull(articleId.value).assertNotNull()
        article.articleTags.map { article.removeTag(it.tagId) }
        articleJpaRepository.delete(article)
    }
}