package com.joo.real_world.article.infrastructure

import com.joo.real_world.article.application.query.dto.ArticleCondition
import com.joo.real_world.article.domain.Article
import com.joo.real_world.article.domain.Favorite
import com.joo.real_world.article.domain.vo.*
import com.joo.real_world.common.application.query.PageSpec
import com.joo.real_world.common.util.assertNotNull
import com.joo.real_world.follow.domain.Follow
import com.joo.real_world.follow.infrastructure.FollowJpaRepository
import com.joo.real_world.tag.domain.TagRepository
import com.joo.real_world.tag.domain.vo.TagId
import com.joo.real_world.user.domain.User
import com.joo.real_world.user.domain.vo.Email
import com.joo.real_world.user.domain.vo.Password
import com.joo.real_world.user.infrastructure.UserJpaRepository
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.transaction.annotation.Transactional

@Transactional
@SpringBootTest
class ArticleQdslRepositoryTest @Autowired constructor(
    private val articleRepository: ArticleJpaRepository,
    private val userRepository: UserJpaRepository,
    private val followRepository: FollowJpaRepository,
    private val articleQueryRepository: ArticleQdslRepository,
    private val favoriteRepository: FavoriteRepository,
    private val tagRepository: TagRepository
) {

    private lateinit var author: User
    private lateinit var follower: User
    private val tag1 = "kotlin"
    private val tag2 = "spring"
    private val slug1 = "slug-1"
    private val title1 = "title1"

    @BeforeEach
    fun setup() {
        author = userRepository.save(User(username = "author", email = Email.of("a@test.com"), password = Password.of("pw")))
        follower = userRepository.save(User(username = "follower", email = Email.of("f@test.com"), password = Password.of("pw")))
        val tags = tagRepository.findOrCreateTags(listOf(tag1, tag2))
        val articleId = articleRepository.save(
            Article(
                slug = Slug(slug1),
                title = Title(title1),
                description = Description("desc1"),
                body = Body("body1"),
                authorId = author.id.assertNotNull(),
                tagIds = listOf(tags.filter { it.name == tag1 }[0].id.assertNotNull())
            )
        )
        articleRepository.save(
            Article(
                slug = Slug("slug-2"),
                title = Title("title2"),
                description = Description("desc2"),
                body = Body("body2"),
                authorId = author.id.assertNotNull(),
                tagIds = listOf(tags.filter { it.name == tag2 }[0].id.assertNotNull())
            )
        )

        followRepository.follow(
            Follow(
                followerId = follower.id.assertNotNull(),
                followeeId = author.id.assertNotNull()
            )
        )

        favoriteRepository.save(Favorite(articleId = articleId.assertNotNull(), userId = follower.id.assertNotNull()))
    }

    @Test
    fun `authorId 조건으로 조회`() {
        val condition = ArticleCondition(userId = author.id.assertNotNull().value, authorId = author.id?.value)
        val result = articleQueryRepository.findByCondition(condition, PageSpec())

        assertThat(result).hasSize(2)
        assertThat(result.all { it.author.username == "author" }).isTrue()
    }

    @Test
    fun `favorited 조건으로 조회`() {
        val condition = ArticleCondition(userId = author.id.assertNotNull().value, favoriteByUserId = follower.id?.value)
        val result = articleQueryRepository.findByCondition(condition, PageSpec())

        assertThat(result).hasSize(1)
        assertThat(result[0].slug).isEqualTo("slug-1")
    }

    @Test
    fun `팔로우 여부 확인`() {
        val condition = ArticleCondition(userId = follower.id.assertNotNull().value, authorId = author.id?.value)
        val result = articleQueryRepository.findByCondition(condition, PageSpec())

        assertThat(result).isNotEmpty
        assertThat(result[0].author.following).isTrue()
    }

    @Test
    fun `태그가 잘 매핑되는지`() {
        val condition = ArticleCondition(userId = author.id.assertNotNull().value, tag = tag1)
        val result = articleQueryRepository.findByCondition(condition, PageSpec())

        val tagLists = result.map { it.tagList ?: emptyList() }.flatten()
        assertThat(tagLists[0]).isEqualTo(tag1)
    }

    @Test
    fun `페이징 적용`() {
        val condition = ArticleCondition(userId = author.id.assertNotNull().value)
        val result = articleQueryRepository.findByCondition(condition, PageSpec(1, 0))

        assertThat(result).hasSize(1)
    }

    @Test
    fun `조건에 맞는 글이 없을 때 emptyList`() {
        val condition = ArticleCondition(userId = author.id.assertNotNull().value, authorId = -999L)
        val result = articleQueryRepository.findByCondition(condition, PageSpec())

        assertThat(result).isEmpty()
    }

    @Test
    fun `slug로 조회`() {
        val result = articleQueryRepository.findBySlugAndUserId(userId = follower.id.assertNotNull().value, slug = slug1)

        assertThat(result.author.following).isTrue
        assertThat(result.tagList).isNotEmpty
        assertThat(result.title).isEqualTo(title1)
    }
}
