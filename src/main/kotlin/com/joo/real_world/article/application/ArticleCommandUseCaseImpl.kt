package com.joo.real_world.article.application

import com.joo.real_world.article.domain.Article
import com.joo.real_world.article.domain.ArticleRepository
import com.joo.real_world.article.domain.Comment
import com.joo.real_world.article.domain.vo.*
import com.joo.real_world.article.infrastructure.CommentRepository
import com.joo.real_world.article.infrastructure.FavoriteRepository
import com.joo.real_world.common.exception.CustomExceptionType
import com.joo.real_world.common.util.assertNotNull
import com.joo.real_world.security.infrastructure.UserSession
import com.joo.real_world.tag.application.TagPort
import com.joo.real_world.tag.domain.vo.TagId
import com.joo.real_world.user.application.UserProviderService
import com.joo.real_world.user.domain.vo.UserId
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Transactional(rollbackFor = [Exception::class])
@Service
class ArticleCommandUseCaseImpl(
    private val articleRepository: ArticleRepository,
    private val commentRepository: CommentRepository,
    private val favoriteRepository: FavoriteRepository,
    private val tagPort: TagPort
) : ArticleCommandUseCase {
    override fun createArticle(createArticleCommand: CreateArticleCommand, userSession: UserSession): Long {
        val title = Title(createArticleCommand.title)
        val slug = generateUniqueSlug(title)

        val tagIds = createArticleCommand.tagList
            ?.let { tagPort.findOrCreateTags(it) }
            .orEmpty()

        val article = Article.create(
            slug = slug,
            title = title,
            description = Description(createArticleCommand.description),
            body = Body(createArticleCommand.body),
            tagIds = tagIds,
            authorId = UserId(userSession.userId)
        )

        return articleRepository.save(article).value
    }

    private fun generateUniqueSlug(title: Title): Slug {
        val baseSlug = Slug.fromTitle(title)
        return (0..9)
            .map { if (it == 0) baseSlug else Slug("$baseSlug-$it") }
            .first { articleRepository.findBySlug(it) == null }
    }


    override fun updateArticle(updateArticleCommand: UpdateArticleCommand, userSession: UserSession): Long {
        val article = articleRepository.findBySlug(Slug(updateArticleCommand.slug)).assertNotNull(CustomExceptionType.NOT_FOUND_SLUG)

        val changedArticle = article.change(
            authorId = UserId(userSession.userId),
            title = updateArticleCommand.title?.let { Title(it) },
            description = updateArticleCommand.description?.let { Description(it) },
            body = updateArticleCommand.body?.let { Body(it) }
        )

        return articleRepository.save(changedArticle).value
    }

    override fun deleteArticle(slug: String, userSession: UserSession) {
        val article = articleRepository.findBySlug(Slug(slug)).assertNotNull(CustomExceptionType.NOT_FOUND_SLUG)
        article.delete(UserId(userSession.userId))
        articleRepository.delete(article.id.assertNotNull())
    }

    override fun addComment(addCommentCommand: AddCommentCommand, userSession: UserSession): Long {
        val article = articleRepository.findBySlug(Slug(addCommentCommand.slug)).assertNotNull(CustomExceptionType.NOT_FOUND_SLUG)
        val comment = article.addComment(authorId = UserId(userSession.userId), body = Body(addCommentCommand.body))
        return commentRepository.save(comment).value
    }

    override fun deleteComment(slug: String, commentId: Long, userSession: UserSession) {
        val article = articleRepository.findBySlug(Slug(slug)).assertNotNull(CustomExceptionType.NOT_FOUND_SLUG)
        article.removeComment(CommentId(commentId))
        articleRepository.save(article)
    }

    override fun favoriteArticle(slug: String, userSession: UserSession): Long {
        val article = articleRepository.findBySlug(Slug(slug)).assertNotNull(CustomExceptionType.NOT_FOUND_SLUG)
        val favorite = article.favorite(UserId(userSession.userId))
        return favoriteRepository.save(favorite).value
    }

    override fun unFavoriteArticle(slug: String, userSession: UserSession): Long {
        val article = articleRepository.findBySlug(Slug(slug)).assertNotNull(CustomExceptionType.NOT_FOUND_SLUG)
        article.unfavorite(UserId(userSession.userId))
        return articleRepository.save(article).value
    }
}