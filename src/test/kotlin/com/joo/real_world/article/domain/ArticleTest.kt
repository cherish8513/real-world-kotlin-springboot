package com.joo.real_world.article.domain

import com.joo.real_world.article.domain.vo.*
import com.joo.real_world.common.exception.CustomException
import com.joo.real_world.common.util.assertNotNull
import com.joo.real_world.tag.domain.vo.TagId
import com.joo.real_world.user.domain.vo.UserId
import org.assertj.core.api.Assertions.assertThat
import org.assertj.core.api.Assertions.assertThatThrownBy
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

class ArticleTest {

    private lateinit var article: Article
    private val authorId = UserId(1L)
    private val otherUserId = UserId(2L)

    @BeforeEach
    fun setUp() {
        // Given: an article created by the author
        article = Article.create(
            slug = Slug.fromTitle(Title("Test Article")),
            title = Title("Test Article"),
            description = Description("Test Description"),
            body = Body("Test Body"),
            tagIds = listOf(TagId(1L)),
            authorId = authorId
        )

        // Set article id manually for testing purposes
        val field = article::class.java.getDeclaredField("id")
        field.isAccessible = true
        field.set(article, ArticleId(1))
    }

    @Test
    fun `author can update article`() {
        // When: the author updates the article
        val updated = article.change(authorId, title = Title("Updated Title"))

        // Then: the title is updated and other fields remain unchanged
        assertThat(updated.title.value).isEqualTo("Updated Title")
        assertThat(updated.description).isEqualTo(article.description)
        assertThat(updated.body).isEqualTo(article.body)
    }

    @Test
    fun `non-author cannot update article`() {
        // When & Then: a non-author tries to update the article -> exception thrown
        assertThatThrownBy {
            article.change(otherUserId, title = Title("Illegal Update"))
        }.isInstanceOf(CustomException::class.java)
            .hasMessageContaining("기사의 작성자가 아닙니다")
    }

    @Test
    fun `can add comment`() {
        // When: a comment is added
        val comment = article.addComment(authorId, Body("Comment Body"))

        // Then: the comment should exist in the article
        assertThat(article.comments).hasSize(1)
        assertThat(comment.body.value).isEqualTo("Comment Body")
    }

    @Test
    fun `can remove comment`() {
        // Given: a comment exists
        val comment = article.addComment(authorId, Body("Comment to remove"))
        val field = comment::class.java.getDeclaredField("id")
        field.isAccessible = true
        field.set(comment, CommentId(1))

        // When: the comment is removed
        article.removeComment(comment.id.assertNotNull())

        // Then: the comments list should be empty
        assertThat(article.comments).isEmpty()
    }

    @Test
    fun `non-author cannot delete article`() {
        // When & Then: a non-author tries to delete the article -> exception thrown
        assertThatThrownBy {
            article.delete(otherUserId)
        }.isInstanceOf(CustomException::class.java)
            .hasMessageContaining("기사의 작성자가 아닙니다")
    }

    @Test
    fun `can add favorite`() {
        // When: a user favorites the article
        val favorite = article.favorite(otherUserId)

        // Then: the favorite is added
        assertThat(article.favorites).hasSize(1)
        assertThat(favorite.userId).isEqualTo(otherUserId)
    }

    @Test
    fun `cannot add duplicate favorite`() {
        // Given: user has already favorited
        article.favorite(otherUserId)

        // When & Then: user tries to favorite again -> exception thrown
        assertThatThrownBy {
            article.favorite(otherUserId)
        }.isInstanceOf(CustomException::class.java)
            .hasMessageContaining("이미 Favorite에 등록되어 있습니다.")
    }

    @Test
    fun `can remove favorite`() {
        // Given: user has favorited the article
        article.favorite(otherUserId)

        // When: the favorite is removed
        article.unfavorite(otherUserId)

        // Then: favorites list should be empty
        assertThat(article.favorites).isEmpty()
    }

    @Test
    fun `isFavorite returns correct result`() {
        // Then: initially, not favorited
        assertThat(article.isFavorite(otherUserId)).isFalse()

        // When: user favorites the article
        article.favorite(otherUserId)

        // Then: isFavorite returns true
        assertThat(article.isFavorite(otherUserId)).isTrue()
    }
}
