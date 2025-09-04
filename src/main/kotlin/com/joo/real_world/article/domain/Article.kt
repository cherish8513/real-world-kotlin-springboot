package com.joo.real_world.article.domain

import com.joo.real_world.article.domain.vo.*
import com.joo.real_world.common.exception.CustomExceptionType
import com.joo.real_world.user.domain.vo.UserId
import java.time.LocalDateTime

class Article(
    val id: ArticleId? = null,
    val slug: Slug,
    val title: Title,
    val description: Description,
    val body: Body,
    val tags: List<Tag> = emptyList(),
    val favorited: Boolean = false,
    val favoritesCount: Int = 0,
    val authorId: UserId,
    val createdAt: LocalDateTime = LocalDateTime.now(),
    val updatedAt: LocalDateTime = LocalDateTime.now()
) {
    private val _comments: MutableList<Comment> = mutableListOf()
    val comments: List<Comment> get() = _comments.toList()

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
            tags = this.tags,
            favorited = this.favorited,
            favoritesCount = this.favoritesCount,
            authorId = this.authorId,
            createdAt = this.createdAt,
            updatedAt = LocalDateTime.now()
        )
    }


    fun addComment(comment: Comment): Comment {
        _comments.add(comment)
        return comment
    }

    fun removeComment(commentId: CommentId) {
        _comments.removeIf { it.id == commentId }
    }

    fun delete(authorId: UserId) {
        if (this.authorId.value != authorId.value) {
            throw CustomExceptionType.BAD_REQUEST.toException("기사의 작성자가 아닙니다")
        }
    }

    companion object {
        fun create(
            title: Title,
            description: Description,
            body: Body,
            tags: List<Tag>?,
            authorId: UserId
        ): Article {
            return Article(
                slug = Slug.fromTitle(title),
                title = title,
                description = description,
                body = body,
                tags = tags ?: emptyList(),
                authorId = authorId
            )
        }
    }
}