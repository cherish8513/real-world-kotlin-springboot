package com.joo.real_world.article.application

import com.joo.real_world.article.domain.Article
import com.joo.real_world.article.domain.ArticleRepository
import com.joo.real_world.article.domain.vo.*
import com.joo.real_world.article.presentation.request.CreateArticleRequest
import com.joo.real_world.article.presentation.request.UpdateArticleRequest
import com.joo.real_world.common.exception.CustomExceptionType
import com.joo.real_world.common.util.assertNotNull
import com.joo.real_world.security.infrastructure.UserSession
import com.joo.real_world.user.application.UserProviderService
import com.joo.real_world.user.domain.vo.UserId
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Transactional(rollbackFor = [Exception::class])
@Service
class ManagementArticleUseCaseImpl(
    private val articleRepository: ArticleRepository,
    private val userProviderService: UserProviderService,
) : ManagementArticleUseCase {
    override fun createArticle(
        createArticleRequest: CreateArticleRequest,
        userSession: UserSession
    ): ArticleDto {
        val request = createArticleRequest.createArticleRequestDto
        val author = userProviderService.getUser(userSession.userId)
        return articleRepository.save(
            Article.create(
                title = Title(request.title),
                description = Description(request.description),
                body = Body(request.body),
                tags = request.tagList?.map { Tag(it) },
                authorId = UserId(author.id)
            )
        ).toDto(author = author, following = false)
    }

    override fun updateArticle(
        updateArticleRequest: UpdateArticleRequest,
        slug: String,
        userSession: UserSession
    ): ArticleDto {
        val article = articleRepository.findBySlug(Slug(slug)).assertNotNull(CustomExceptionType.NOT_FOUND_SLUG)
        article.validateArticleOwn(UserId(userSession.userId))
        val request = updateArticleRequest.updateArticleRequestDto
        val changedArticle = article.change(
            title = request.title?.let { Title(it) },
            description = request.description?.let { Description(it) },
            body = request.body?.let { Body(it) }
        )

        val author = userProviderService.getUser(userSession.userId)
        return articleRepository.save(changedArticle).toDto(author = author, following = false)
    }

    private fun Article.validateArticleOwn(userId: UserId) {
        if (!isOwn(userId))
            throw CustomExceptionType.BAD_REQUEST.toException("기사의 작성자가 아닙니다")
    }

    override fun deleteArticle(
        slug: String,
        userSession: UserSession
    ) {
        val article = articleRepository.findBySlug(Slug(slug)).assertNotNull(CustomExceptionType.NOT_FOUND_SLUG)
        article.validateArticleOwn(UserId(userSession.userId))

        articleRepository.delete(article.id.assertNotNull())
    }
}