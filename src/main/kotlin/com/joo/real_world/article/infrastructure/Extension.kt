package com.joo.real_world.article.infrastructure

import com.joo.real_world.article.domain.Article
import com.joo.real_world.article.domain.Comment
import com.joo.real_world.article.domain.Favorite
import com.joo.real_world.article.domain.vo.*
import com.joo.real_world.common.util.assertNotNull
import com.joo.real_world.tag.domain.vo.TagId
import com.joo.real_world.user.domain.vo.UserId

fun Article.toEntity(): ArticleEntity {
    val articleEntity = ArticleEntity(
        id = this.id?.value,
        slug = this.slug.value,
        title = this.title.value,
        description = this.description.value,
        body = this.body.value,
        authorId = authorId.value
    )

    articleEntity.favorites.addAll(this.favorites.map {
        FavoriteEntity(
            id = it.id?.value,
            userId = it.userId.value,
            articleId = this.id.assertNotNull().value
        )
    })

    articleEntity.comments.addAll(comments.map {
        CommentEntity(
            id = it.id?.value,
            body = it.body.value,
            articleId = id.assertNotNull().value,
            authorId = it.authorId.value
        )
    })

    return articleEntity
}

fun ArticleEntity.toDomain(): Article {
    val article = Article(
        id = ArticleId(id.assertNotNull()),
        slug = Slug(slug),
        title = Title(title),
        description = Description(description),
        body = Body(body),
        tagIds = articleTags.map { TagId(it.tagId) },
        authorId = UserId(authorId),
        comments = this.comments.map {
            Comment(
                id = CommentId(it.id.assertNotNull()),
                articleId = ArticleId(it.articleId),
                authorId = UserId(it.authorId),
                body = Body(it.body),
                createdAt = it.createdAt,
                updatedAt = it.updatedAt
            )
        }.toMutableList(),
        favorites = favorites.map {
            Favorite(
                userId = UserId(it.userId),
                articleId = ArticleId(it.articleId)
            )
        }.toMutableSet(),
        createdAt = createdAt,
        updatedAt = updatedAt,
    )

    return article
}