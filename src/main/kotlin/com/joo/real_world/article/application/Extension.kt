package com.joo.real_world.article.application

import com.joo.real_world.article.domain.Article
import com.joo.real_world.article.domain.Comment
import com.joo.real_world.common.util.assertNotNull
import com.joo.real_world.user.application.UserDto

fun Article.toDto(author: UserDto, following: Boolean): ArticleDto {
    return ArticleDto(
        slug = this.slug.value,
        title = this.title.value,
        description = this.description.value,
        body = this.body.value,
        tagList = this.tags?.map { it.value },
        createdAt = this.createdAt.toString(),
        updatedAt = this.updatedAt.toString(),
        favorited = this.favorited,
        favoritesCount = this.favoritesCount,
        author = AuthorDto(
            username = author.username,
            bio = author.bio,
            image = author.image,
            following = following
        )
    )
}

fun Comment.toDto(author: UserDto, following: Boolean): CommentDto =
    CommentDto(
        commentId = id.assertNotNull().value,
        body = body.value,
        createdAt = createdAt.toString(),
        updatedAt = updatedAt.toString(),
        author = AuthorDto(
            username = author.username,
            bio = author.bio,
            image = author.image,
            following = following
        )
    )