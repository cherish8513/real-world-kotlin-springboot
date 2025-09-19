package com.joo.real_world.article.infrastructure

import com.joo.real_world.article.application.ArticleDto
import com.joo.real_world.article.application.AuthorDto
import com.joo.real_world.article.application.CommentDto
import com.joo.real_world.article.application.query.ArticleQueryRepository
import com.joo.real_world.article.application.query.dto.ArticleCondition
import com.joo.real_world.article.infrastructure.QArticleEntity.articleEntity
import com.joo.real_world.article.infrastructure.QArticleTagEntity.articleTagEntity
import com.joo.real_world.article.infrastructure.QCommentEntity.commentEntity
import com.joo.real_world.article.infrastructure.QFavoriteEntity.favoriteEntity
import com.joo.real_world.common.application.query.PageSpec
import com.joo.real_world.common.util.assertNotNull
import com.joo.real_world.follow.infrastructure.QFollowEntity.followEntity
import com.joo.real_world.tag.infrastructure.QTagEntity.tagEntity
import com.joo.real_world.user.infrastructure.QUserEntity.userEntity
import com.querydsl.core.annotations.QueryProjection
import com.querydsl.core.types.dsl.CaseBuilder
import com.querydsl.jpa.impl.JPAQueryFactory
import org.springframework.stereotype.Repository


@Repository
class ArticleQdslRepository(
    private val query: JPAQueryFactory,
    private val favoriteJpaRepository: IFavoriteJpaRepository,
) : ArticleQueryRepository {
    override fun findBySlugAndUserId(slug: String, userId: Long): ArticleDto {
       return findArticle(slug = slug, userId = userId)
    }

    override fun findByArticleIdAndUserId(articleId: Long, userId: Long): ArticleDto {
        return findArticle(articleId = articleId, userId = userId)
    }

    private fun findArticle(articleId: Long? = null, slug: String? = null, userId: Long): ArticleDto {
        val article = query
            .select(
                QArticleQDto(
                    articleEntity.id,
                    articleEntity.slug,
                    articleEntity.title,
                    articleEntity.description,
                    articleEntity.body,
                    articleEntity.createdAt.stringValue(),
                    articleEntity.updatedAt.stringValue(),
                    QAuthorQDto(
                        userEntity.id,
                        userEntity.username,
                        userEntity.bio,
                        userEntity.image,
                        CaseBuilder().`when`(followEntity.id.isNotNull).then(true).otherwise(false)
                    )
                )
            )
            .from(articleEntity)
            .join(userEntity).on(userEntity.id.eq(articleEntity.authorId))
            .leftJoin(favoriteEntity).on(favoriteEntity.userId.eq(userEntity.id))
            .leftJoin(followEntity).on(followEntity.id.followerId.eq(userId).and(followEntity.id.followeeId.eq(articleEntity.authorId)))
            .where(
                slug?.let { articleEntity.slug.eq(it) },
                articleId?.let { articleEntity.id.eq(it) }
            )
            .fetchOne()
            .assertNotNull()

        val tags = findTags(listOf(article.articleId))

        return ArticleDto(
            slug = article.slug,
            title = article.title,
            description = article.description,
            body = article.body,
            tagList = tags.map { it.name },
            createdAt = article.createdAt,
            updatedAt = article.updatedAt,
            favorited = favoriteJpaRepository.existsByArticleIdAndUserId(articleId = article.articleId, userId = userId),
            favoritesCount = favoriteJpaRepository.countByArticleId(article.articleId),
            author = AuthorDto(
                username = article.author.username,
                bio = article.author.bio,
                image = article.author.image,
                following = article.author.following
            )
        )
    }

    override fun findByCondition(articleCondition: ArticleCondition, pageSpec: PageSpec): List<ArticleDto> {
        return findArticlesByCondition(articleCondition, pageSpec, false)
    }

    override fun findFeed(articleCondition: ArticleCondition, pageSpec: PageSpec): List<ArticleDto> {
        return findArticlesByCondition(articleCondition, pageSpec, true)
    }

    override fun findComment(commentId: Long, userId: Long): CommentDto {
        return findComments(commentId = commentId, userId = userId)[0]
    }

    override fun findComments(slug: String, userId: Long): List<CommentDto> {
        return findComments(commentId = null, slug = slug, userId = userId)
    }

    private fun findComments(commentId: Long? = null, slug: String? = null, userId: Long): List<CommentDto> {
        return query
            .select(
                QCommentQDto(
                    commentEntity.id,
                    commentEntity.body,
                    commentEntity.createdAt.stringValue(),
                    commentEntity.updatedAt.stringValue(),
                    QAuthorQDto(
                        userEntity.id,
                        userEntity.username,
                        userEntity.bio,
                        userEntity.image,
                        CaseBuilder().`when`(followEntity.id.isNotNull).then(true).otherwise(false)
                    )
                )
            )
            .from(commentEntity)
            .join(articleEntity).on(articleEntity.id.eq(commentEntity.articleId))
            .join(userEntity).on(userEntity.id.eq(commentEntity.authorId))
            .leftJoin(followEntity).on(followEntity.id.followerId.eq(userId).and(followEntity.id.followeeId.eq(commentEntity.authorId)))
            .where(
                slug?.let { articleEntity.slug.eq(it) },
                commentId?.let { commentEntity.id.eq(it) }
            )
            .orderBy(commentEntity.createdAt.desc())
            .fetch()
            .map { CommentDto(
                commentId = it.commentId,
                body = it.body,
                createdAt = it.createdAt,
                updatedAt = it.updatedAt,
                author = AuthorDto(
                    username = it.author.username,
                    bio = it.author.bio,
                    image = it.author.image,
                    following = it.author.following
                )
            ) }
    }

    private fun findArticlesByCondition(articleCondition: ArticleCondition, pageSpec: PageSpec, onlyFollow: Boolean) : List<ArticleDto> {
        val articles = findArticleWithAuthor(articleCondition, pageSpec, onlyFollow)
        if (articles.isEmpty()) return emptyList()
        val tags = findTags(articles.map { it.articleId })
        val tagMap = tags.groupBy { it.articleId }

        return articles.map {
            ArticleDto(
                slug = it.slug,
                title = it.title,
                description = it.description,
                body = it.body,
                tagList = tagMap[it.articleId]?.map { tag -> tag.name },
                createdAt = it.createdAt,
                updatedAt = it.updatedAt,
                favorited = favoriteJpaRepository.existsByArticleIdAndUserId(articleId = it.articleId, userId = articleCondition.userId),
                favoritesCount = favoriteJpaRepository.countByArticleId(it.articleId),
                author = AuthorDto(
                    username = it.author.username,
                    bio = it.author.bio,
                    image = it.author.image,
                    following = it.author.following
                )
            )
        }
    }

    private fun findArticleWithAuthor(
        articleCondition: ArticleCondition,
        pageSpec: PageSpec,
        onlyFollow: Boolean
    ): List<ArticleQDto> {
        var baseQuery = query
            .select(
                QArticleQDto(
                    articleEntity.id,
                    articleEntity.slug,
                    articleEntity.title,
                    articleEntity.description,
                    articleEntity.body,
                    articleEntity.createdAt.stringValue(),
                    articleEntity.updatedAt.stringValue(),
                    QAuthorQDto(
                        userEntity.id,
                        userEntity.username,
                        userEntity.bio,
                        userEntity.image,
                        CaseBuilder().`when`(followEntity.id.isNotNull).then(true).otherwise(false)
                    )
                )
            )
            .from(articleEntity)
            .join(userEntity).on(userEntity.id.eq(articleEntity.authorId))

        if (onlyFollow) {
            baseQuery = baseQuery.join(followEntity).on(
                followEntity.id.followerId.eq(articleCondition.userId)
                    .and(followEntity.id.followeeId.eq(articleEntity.authorId))
            )
        }
        else {
            baseQuery = baseQuery.leftJoin(followEntity).on(
                followEntity.id.followerId.eq(articleCondition.userId)
                    .and(followEntity.id.followeeId.eq(articleEntity.authorId))
            )
        }

        if (articleCondition.favoriteByUserId != null) {
            baseQuery = baseQuery
                .join(favoriteEntity).on(
                    favoriteEntity.userId.eq(articleCondition.favoriteByUserId)
                        .and(favoriteEntity.articleId.eq(articleEntity.id))
                )
        }

        if (articleCondition.tag != null) {
            baseQuery = baseQuery
                .join(articleTagEntity).on(articleTagEntity.article.id.eq(articleEntity.id))
                .join(tagEntity).on(tagEntity.id.eq(articleTagEntity.tagId))
                .where(tagEntity.name.eq(articleCondition.tag))
        }

        return baseQuery
            .where(articleCondition.authorId?.let { userEntity.id.eq(it) },)
            .offset(pageSpec.offset)
            .limit(pageSpec.limit)
            .orderBy(articleEntity.createdAt.desc())
            .fetch()
    }

    private fun findTags(articleIds: List<Long>): List<TagQDto> {
        return query
            .select(
                QTagQDto(
                    articleEntity.id,
                    tagEntity.name
                )
            )
            .from(articleEntity)
            .join(articleTagEntity).on(articleTagEntity.article.id.eq(articleEntity.id))
            .join(tagEntity).on(tagEntity.id.eq(articleTagEntity.tagId))
            .where(articleEntity.id.`in`(articleIds))
            .fetch()
    }
}

data class ArticleQDto @QueryProjection constructor(
    val articleId: Long,
    val slug: String,
    val title: String,
    val description: String,
    val body: String,
    val createdAt: String,
    val updatedAt: String,
    val author: AuthorQDto
)

data class TagQDto @QueryProjection constructor(
    val articleId: Long,
    val name: String
)

data class AuthorQDto @QueryProjection constructor(
    val authorId: Long,
    val username: String,
    val bio: String?,
    val image: String?,
    val following: Boolean
)

data class CommentQDto @QueryProjection constructor(
    val commentId: Long,
    val body: String,
    val createdAt: String,
    val updatedAt: String,
    val author: AuthorQDto
)