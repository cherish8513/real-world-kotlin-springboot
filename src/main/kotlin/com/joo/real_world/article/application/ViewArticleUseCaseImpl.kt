package com.joo.real_world.article.application

import com.joo.real_world.article.application.query.ArticleQueryRepository
import com.joo.real_world.article.application.query.dto.ArticleCondition
import com.joo.real_world.article.domain.ArticleRepository
import com.joo.real_world.article.domain.vo.Slug
import com.joo.real_world.common.application.query.PageSpec
import com.joo.real_world.common.exception.CustomExceptionType
import com.joo.real_world.common.util.assertNotNull
import com.joo.real_world.follow.application.FollowRelationService
import com.joo.real_world.security.infrastructure.UserSession
import com.joo.real_world.user.application.UserProviderService
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Transactional(readOnly = true, rollbackFor = [Exception::class])
@Service
class ViewArticleUseCaseImpl(
    private val articleRepository: ArticleRepository,
    private val articleQueryRepository: ArticleQueryRepository,
    private val userProviderService: UserProviderService,
    private val followRelationService: FollowRelationService
) : ViewArticleUseCase {
    override fun getArticle(slug: String, userSession: UserSession): ArticleDto {
        val article = articleRepository.findBySlug(Slug(slug)).assertNotNull(CustomExceptionType.NOT_FOUND_SLUG)
        val author = userProviderService.getUser(article.authorId.value)
        return article.toDto(
            author = author,
            following = followRelationService.isFollowing(followerId = userSession.userId, followeeId = author.id)
        )
    }

    override fun getArticles(getArticleQuery: GetArticleQuery, userSession: UserSession): List<ArticleDto> {
        return articleQueryRepository.findByCondition(
            articleCondition = ArticleCondition(
                userId = userSession.userId,
                tag = getArticleQuery.tag,
                authorId = getArticleQuery.author?.let { userProviderService.getUser(it).id },
                favorited = getArticleQuery.favorited
            ),
            pageSpec = PageSpec(
                limit = getArticleQuery.limit,
                offset = getArticleQuery.offset
            )
        )
    }
}