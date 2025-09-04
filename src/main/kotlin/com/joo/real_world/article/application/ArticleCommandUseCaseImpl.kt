package com.joo.real_world.article.application

import com.joo.real_world.article.domain.Article
import com.joo.real_world.article.domain.ArticleRepository
import com.joo.real_world.article.domain.Comment
import com.joo.real_world.article.domain.vo.*
import com.joo.real_world.common.exception.CustomExceptionType
import com.joo.real_world.common.util.assertNotNull
import com.joo.real_world.security.infrastructure.UserSession
import com.joo.real_world.user.application.UserProviderService
import com.joo.real_world.user.domain.vo.UserId
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Transactional(rollbackFor = [Exception::class])
@Service
class ArticleCommandUseCaseImpl(
    private val articleRepository: ArticleRepository,
    private val userProviderService: UserProviderService,
) : ArticleCommandUseCase {
    override fun createArticle(createArticleCommand: CreateArticleCommand, userSession: UserSession): ArticleDto {
        val author = userProviderService.getUser(userSession.userId)
        return articleRepository.save(
            Article.create(
                title = Title(createArticleCommand.title),
                description = Description(createArticleCommand.description),
                body = Body(createArticleCommand.body),
                tags = createArticleCommand.tagList?.map { Tag(it) },
                authorId = UserId(author.id)
            )
        ).toDto(author = author, following = false)
    }

    override fun updateArticle(updateArticleCommand: UpdateArticleCommand, userSession: UserSession): ArticleDto {
        val article = articleRepository.findBySlug(Slug(updateArticleCommand.slug)).assertNotNull(CustomExceptionType.NOT_FOUND_SLUG)

        val changedArticle = article.change(
            authorId = UserId(userSession.userId),
            title = updateArticleCommand.title?.let { Title(it) },
            description = updateArticleCommand.description?.let { Description(it) },
            body = updateArticleCommand.body?.let { Body(it) }
        )

        val author = userProviderService.getUser(userSession.userId)
        return articleRepository.save(changedArticle).toDto(author = author, following = false)
    }

    override fun deleteArticle(slug: String, userSession: UserSession) {
        val article = articleRepository.findBySlug(Slug(slug)).assertNotNull(CustomExceptionType.NOT_FOUND_SLUG)
        article.delete(UserId(userSession.userId))
        articleRepository.delete(article.id.assertNotNull())
    }

    override fun addComment(addCommentCommand: AddCommentCommand, userSession: UserSession): CommentDto {
        val article = articleRepository.findBySlug(Slug(addCommentCommand.slug)).assertNotNull(CustomExceptionType.NOT_FOUND_SLUG)
        val author = userProviderService.getUser(userSession.userId)
        val comment = article.addComment(Comment(authorId = UserId(author.id), body = Body(addCommentCommand.body)))
        articleRepository.save(article)
        return comment.toDto(author = author, following = false)
    }

    override fun deleteComment(slug: String, commentId: Long, userSession: UserSession) {
        val article = articleRepository.findBySlug(Slug(slug)).assertNotNull(CustomExceptionType.NOT_FOUND_SLUG)
        article.removeComment(CommentId(commentId))
        articleRepository.save(article)
    }
}