package com.joo.real_world.article.infrastructure

import com.joo.real_world.article.domain.Article
import com.joo.real_world.article.domain.vo.*
import com.joo.real_world.common.util.assertNotNull
import com.joo.real_world.user.domain.vo.UserId

fun Article.toEntity(slug: String? = null): ArticleEntity =
    ArticleEntity(
        id = this.id?.value,
        slug = slug ?: this.slug.value,
        title = this.title.value,
        description = this.description.value,
        body = this.body.value,
        favorited = this.favorited,
        favoritesCount = this.favoritesCount,
        authorId = authorId.value
    )


fun ArticleEntity.toDomain(): Article =
    Article(
        id = ArticleId(id.assertNotNull()),
        slug = Slug(slug),
        title = Title(title),
        description = Description(description),
        body = Body(body),
        tags = articleTags.map { Tag(it.tag.name) },
        favorited = favorited,
        favoritesCount = favoritesCount,
        authorId = UserId(authorId),
        createdAt = createdAt,
        updatedAt = updatedAt,
    )