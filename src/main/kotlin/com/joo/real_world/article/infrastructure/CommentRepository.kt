package com.joo.real_world.article.infrastructure

import com.joo.real_world.article.domain.Comment
import com.joo.real_world.article.domain.vo.CommentId
import com.joo.real_world.common.util.assertNotNull
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

interface CommentRepository {
    fun save(comment: Comment): CommentId
}

interface ICommentJpaRepository: JpaRepository<CommentEntity, Long>

@Repository
class CommentJpaRepositoryImpl(
    private val commentJpaRepository: ICommentJpaRepository
): CommentRepository {
    override fun save(comment: Comment): CommentId {
        return commentJpaRepository.save(CommentEntity(
            body = comment.body.value,
            articleId = comment.articleId.value,
            authorId = comment.authorId.value,
        )).let { CommentId(it.id.assertNotNull())}
    }

}