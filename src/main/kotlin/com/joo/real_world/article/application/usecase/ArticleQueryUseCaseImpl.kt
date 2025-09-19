package com.joo.real_world.article.application.usecase

import com.joo.real_world.article.application.ArticleDto
import com.joo.real_world.article.application.CommentDto
import com.joo.real_world.article.application.GetArticleQuery
import com.joo.real_world.article.application.query.ArticleQueryRepository
import com.joo.real_world.article.application.query.dto.ArticleCondition
import com.joo.real_world.common.application.query.PageSpec
import com.joo.real_world.security.infrastructure.UserSession
import com.joo.real_world.user.application.UserQueryService
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Transactional(readOnly = true, rollbackFor = [Exception::class])
@Service
class ArticleQueryUseCaseImpl(
    private val articleQueryRepository: ArticleQueryRepository,
    private val userQueryService: UserQueryService,
) : ArticleQueryUseCase {
    override fun getArticle(slug: String, userSession: UserSession): ArticleDto {
        return articleQueryRepository.findBySlugAndUserId(slug, userSession.userId)
    }

    override fun getArticle(articleId: Long, userSession: UserSession): ArticleDto {
        return articleQueryRepository.findByArticleIdAndUserId(articleId, userSession.userId)
    }

    override fun getArticles(getArticleQuery: GetArticleQuery, userSession: UserSession): List<ArticleDto> {
        return articleQueryRepository.findByCondition(
            articleCondition = ArticleCondition(
                userId = userSession.userId,
                tag = getArticleQuery.tag,
                authorId = getArticleQuery.author?.let { userQueryService.getUser(it).id },
                favoriteByUserId = getArticleQuery.favorited?.let { userQueryService.getUser(it).id }
            ),
            pageSpec = PageSpec(
                limit = getArticleQuery.limit,
                offset = getArticleQuery.offset
            )
        )
    }

    override fun getFeedArticles(getArticleQuery: GetArticleQuery, userSession: UserSession): List<ArticleDto> {
        return articleQueryRepository.findFeed(
            articleCondition = ArticleCondition(
                userId = userSession.userId,
                tag = getArticleQuery.tag,
                authorId = getArticleQuery.author?.let { userQueryService.getUser(it).id },
                favoriteByUserId = getArticleQuery.favorited?.let { userQueryService.getUser(it).id }
            ),
            pageSpec = PageSpec(
                limit = getArticleQuery.limit,
                offset = getArticleQuery.offset
            )
        )
    }

    override fun getComment(commentId: Long, userSession: UserSession): CommentDto {
        return articleQueryRepository.findComment(commentId, userSession.userId)
    }

    override fun getComments(slug: String, userSession: UserSession): List<CommentDto> {
        return articleQueryRepository.findComments(slug, userSession.userId)
    }
}