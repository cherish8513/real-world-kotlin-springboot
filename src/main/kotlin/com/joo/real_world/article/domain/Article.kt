package com.joo.real_world.article.domain

import com.joo.real_world.article.domain.vo.*
import com.joo.real_world.common.exception.CustomExceptionType
import com.joo.real_world.common.util.assertNotNull
import com.joo.real_world.tag.domain.vo.TagId
import com.joo.real_world.user.domain.vo.UserId
import java.time.LocalDateTime

class Article(
    val id: ArticleId? = null,
    val slug: Slug,
    val title: Title,
    val description: Description,
    val body: Body,
    val tagIds: List<TagId> = emptyList(),
    val authorId: UserId,
    val comments: MutableList<Comment> = mutableListOf(),
    val favorites: MutableSet<Favorite> = mutableSetOf(),
    val createdAt: LocalDateTime = LocalDateTime.now(),
    val updatedAt: LocalDateTime = LocalDateTime.now()
) {
    fun change(authorId: UserId, title: Title? = null, description: Description? = null, body: Body? = null): Article {
        if (this.authorId.value != authorId.value) {
            throw CustomExceptionType.BAD_REQUEST.toException("기사의 작성자가 아닙니다")
        }
        return Article(
            id = this.id,
            slug = this.slug,
            title = title ?: this.title,
            description = description ?: this.description,
            body = body ?: this.body,
            tagIds = this.tagIds,
            authorId = this.authorId,
            createdAt = this.createdAt,
            updatedAt = LocalDateTime.now()
        )
    }


    fun addComment(authorId: UserId, body: Body): Comment {
        val comment = Comment(
            articleId = this.id.assertNotNull(),
            authorId = authorId,
            body = body
        )
        comments.add(comment)
        return comment
    }

    fun removeComment(commentId: CommentId) {
        comments.removeIf { it.id == commentId }
    }

    fun delete(authorId: UserId) {
        if (this.authorId.value != authorId.value) {
            throw CustomExceptionType.BAD_REQUEST.toException("기사의 작성자가 아닙니다.")
        }
    }

    fun favorite(userId: UserId): Favorite {
        val favorite = Favorite(userId = userId, articleId = this.id.assertNotNull())
        if (favorites.any { it.userId == userId }) {
            throw CustomExceptionType.BAD_REQUEST.toException("이미 Favorite에 등록되어 있습니다.")
        }
        favorites.add(favorite)
        return favorite
    }

    fun unfavorite(userId: UserId) {
        favorites.remove(Favorite(userId = userId, articleId = this.id.assertNotNull()))
    }

    fun isFavorite(userId: UserId): Boolean {
        return favorites.any { it.userId == userId }
    }

    companion object {
        fun create(
            slug: Slug,
            title: Title,
            description: Description,
            body: Body,
            tagIds: List<TagId>?,
            authorId: UserId
        ): Article {
            return Article(
                slug = slug,
                title = title,
                description = description,
                body = body,
                tagIds = tagIds ?: emptyList(),
                authorId = authorId
            )
        }
    }
}