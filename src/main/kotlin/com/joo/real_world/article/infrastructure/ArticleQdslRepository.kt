package com.joo.real_world.article.infrastructure

import com.joo.real_world.article.application.ArticleDto
import com.joo.real_world.article.application.AuthorDto
import com.joo.real_world.article.application.query.ArticleQueryRepository
import com.joo.real_world.article.application.query.dto.ArticleCondition
import com.joo.real_world.article.infrastructure.QArticleEntity.articleEntity
import com.joo.real_world.article.infrastructure.QArticleTagEntity.articleTagEntity
import com.joo.real_world.article.infrastructure.QTagEntity.tagEntity
import com.joo.real_world.common.application.query.PageSpec
import com.joo.real_world.follow.infrastructure.QFollowEntity.followEntity
import com.joo.real_world.user.infrastructure.QUserEntity.userEntity
import com.querydsl.core.annotations.QueryProjection
import com.querydsl.jpa.impl.JPAQueryFactory
import org.springframework.stereotype.Repository

@Repository
class ArticleQdslRepository(
    private val query: JPAQueryFactory
) : ArticleQueryRepository {
    override fun findByCondition(
        articleCondition: ArticleCondition,
        pageSpec: PageSpec
    ): List<ArticleDto> {
        val articles = findArticleWithAuthor(articleCondition, pageSpec)
        if (articles.isEmpty()) return emptyList()
        val tags = findTags(articles.map { it.slug })
        val tagMap = tags.groupBy { it.articleId }

        return articles.map {
            ArticleDto(
                slug = it.slug,
                title = it.title,
                description = it.description,
                body = it.body,
                tagList = tagMap[it.author.authorId]?.map { it.name },
                createdAt = it.createdAt,
                updatedAt = it.updatedAt,
                favorited = it.favorited,
                favoritesCount = it.favoritesCount,
                author = AuthorDto(
                    username = it.author.username,
                    bio = it.author.bio,
                    image = it.author.image,
                    following = it.author.following
                )
            )
        }
    }

    private fun findArticleWithAuthor(articleCondition: ArticleCondition, pageSpec: PageSpec): List<ArticleQDto> {
        return query
            .select(
                QArticleQDto(
                    articleEntity.slug,
                    articleEntity.title,
                    articleEntity.description,
                    articleEntity.body,
                    articleEntity.createdAt.stringValue(),
                    articleEntity.updatedAt.stringValue(),
                    articleEntity.favorited,
                    articleEntity.favoritesCount,
                    QAuthorQDto(
                        userEntity.id,
                        userEntity.username,
                        userEntity.bio,
                        userEntity.image,
                        followEntity.isNotNull
                    )
                )
            )
            .from(articleEntity)
            .join(userEntity).on(userEntity.id.eq(articleEntity.authorId))
            .leftJoin(followEntity).on(
                followEntity.id.followerId.eq(articleCondition.userId)
                    .and(followEntity.id.followeeId.eq(articleEntity.authorId))
            )
            .where(
                articleCondition.authorId?.let { userEntity.id.eq(it) },
                articleCondition.favorited.let { fav ->
                    if (fav) articleEntity.favorited.isTrue else null
                }
            )
            .offset(pageSpec.offset)
            .limit(pageSpec.limit)
            .fetch()
    }

    private fun findTags(slugs: List<String>): List<TagQDto> {
        return query
            .select(
                QTagQDto(
                    articleEntity.id,
                    tagEntity.id,
                    tagEntity.name
                )
            )
            .from(articleEntity)
            .join(articleTagEntity).on(articleTagEntity.article.id.eq(articleEntity.id))
            .join(tagEntity).on(tagEntity.id.eq(articleTagEntity.tag.id))
            .where(articleEntity.slug.`in`(slugs))
            .fetch()
    }
}

data class ArticleQDto @QueryProjection constructor(
    val slug: String,
    val title: String,
    val description: String,
    val body: String,
    val createdAt: String,
    val updatedAt: String,
    val favorited: Boolean,
    val favoritesCount: Int,
    val author: AuthorQDto
)

data class TagQDto @QueryProjection constructor(
    val articleId: Long,
    val tagId: Long,
    val name: String
)

data class AuthorQDto @QueryProjection constructor(
    val authorId: Long,
    val username: String,
    val bio: String?,
    val image: String?,
    val following: Boolean
)