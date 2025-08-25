package com.joo.real_world.article.domain

import com.joo.real_world.article.domain.vo.ArticleId
import com.joo.real_world.article.domain.vo.Body
import com.joo.real_world.article.domain.vo.Description
import com.joo.real_world.article.domain.vo.Slug
import com.joo.real_world.article.domain.vo.Tag
import com.joo.real_world.article.domain.vo.Title
import com.joo.real_world.common.exception.CustomExceptionType
import com.joo.real_world.user.domain.vo.UserId
import java.time.LocalDateTime

class Article(
    val id: ArticleId? = null,
    val slug: Slug,
    val title: Title,
    val description: Description,
    val body: Body,
    val tags: List<Tag>? = null,
    val favorited: Boolean = false,
    val favoritesCount: Int = 0,
    val authorId: UserId,
    val createdAt: LocalDateTime = LocalDateTime.now(),
    val updatedAt: LocalDateTime = LocalDateTime.now()
) {
    fun change(title: Title? = null, description: Description? = null, body: Body? = null): Article {
        return Article(
            id = this.id,
            slug = this.slug,
            title = title ?: this.title,
            description = description ?: this.description,
            body = body ?: this.body,
            tags = this.tags,
            favorited = this.favorited,
            favoritesCount = this.favoritesCount,
            authorId = this.authorId,
            createdAt = this.createdAt,
            updatedAt = this.updatedAt
        )
    }

    fun isOwn(userId: UserId): Boolean {
        return authorId.value == userId.value
    }

    companion object {
        fun create(title: Title, description: Description, body: Body, tags: List<Tag>? = null, authorId: UserId): Article {
            return Article(
                slug = Slug.fromTitle(title),
                title = title,
                description = description,
                body = body,
                tags = tags,
                authorId = authorId
            )
        }
    }
}