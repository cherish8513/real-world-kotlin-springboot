package com.joo.real_world.article.infrastructure

import com.joo.real_world.article.domain.Article
import com.joo.real_world.article.domain.Comment
import com.joo.real_world.article.domain.Favorite
import com.joo.real_world.article.domain.vo.*
import com.joo.real_world.common.util.assertNotNull
import com.joo.real_world.tag.domain.vo.TagId
import com.joo.real_world.user.domain.vo.UserId
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import java.time.LocalDateTime

class ArticleMappingTest {

    @Test
    fun `Article to ArticleEntity mapping`() {
        // given
        val article = Article(
            id = ArticleId(1L),
            slug = Slug("slug1"),
            title = Title("title1"),
            description = Description("desc1"),
            body = Body("body1"),
            authorId = UserId(10L),
            tagIds = listOf(TagId(100L), TagId(101L)),
            comments = mutableListOf(
                Comment(
                    id = CommentId(1L),
                    articleId = ArticleId(1L),
                    authorId = UserId(20L),
                    body = Body("comment1"),
                    createdAt = LocalDateTime.now(),
                    updatedAt = LocalDateTime.now()
                )
            ),
            favorites = mutableSetOf(
                Favorite(userId = UserId(30L), articleId = ArticleId(1L))
            ),
            createdAt = LocalDateTime.now(),
            updatedAt = LocalDateTime.now()
        )

        // when
        val entity = article.toEntity()

        // then
        assertThat(entity.id).isEqualTo(article.id!!.value)
        assertThat(entity.slug).isEqualTo(article.slug.value)
        assertThat(entity.title).isEqualTo(article.title.value)
        assertThat(entity.description).isEqualTo(article.description.value)
        assertThat(entity.body).isEqualTo(article.body.value)
        assertThat(entity.authorId).isEqualTo(article.authorId.value)
        assertThat(entity.comments).hasSize(1)
        assertThat(entity.favorites).hasSize(1)
    }

    @Test
    fun `ArticleEntity to Article mapping`() {
        // given
        val now = LocalDateTime.now()
        val articleEntity = ArticleEntity(
            id = 1L,
            slug = "slug1",
            title = "title1",
            description = "desc1",
            body = "body1",
            authorId = 10L
        )
        val field = articleEntity::class.java.getDeclaredField("createdAt")
        field.isAccessible = true
        field.set(articleEntity, now)

        val field2 = articleEntity::class.java.getDeclaredField("updatedAt")
        field2.isAccessible = true
        field2.set(articleEntity, now)

        articleEntity.comments.add(
            CommentEntity(
                id = 1L,
                body = "comment1",
                articleId = 1L,
                authorId = 20L
            )
        )
        articleEntity.favorites.add(
            FavoriteEntity(
                id = 1L,
                userId = 30L,
                articleId = 1L
            )
        )
        articleEntity.articleTags.add(ArticleTagEntity(article = articleEntity, tagId = 100L))

        articleEntity.comments.forEach { comment ->
            val createdField = comment::class.java.getDeclaredField("createdAt")
            createdField.isAccessible = true
            createdField.set(comment, now)

            val updatedField = comment::class.java.getDeclaredField("updatedAt")
            updatedField.isAccessible = true
            updatedField.set(comment, now)
        }

        // when
        val article = articleEntity.toDomain()

        // then
        assertThat(article.id.assertNotNull().value).isEqualTo(articleEntity.id)
        assertThat(article.slug.value).isEqualTo(articleEntity.slug)
        assertThat(article.title.value).isEqualTo(articleEntity.title)
        assertThat(article.description.value).isEqualTo(articleEntity.description)
        assertThat(article.body.value).isEqualTo(articleEntity.body)
        assertThat(article.authorId.value).isEqualTo(articleEntity.authorId)
        assertThat(article.comments).hasSize(1)
        assertThat(article.favorites).hasSize(1)
        assertThat(article.tagIds.map { it.value }).containsExactly(100L)
    }
}
