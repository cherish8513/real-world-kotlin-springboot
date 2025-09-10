package com.joo.real_world.article.infrastructure

import com.joo.real_world.article.application.ArticleDto
import com.joo.real_world.article.application.query.dto.ArticleCondition
import com.joo.real_world.article.domain.Article
import com.joo.real_world.article.domain.Comment
import com.joo.real_world.article.domain.Favorite
import com.joo.real_world.article.domain.vo.*
import com.joo.real_world.common.application.query.PageSpec
import com.joo.real_world.common.util.assertNotNull
import com.joo.real_world.follow.domain.Follow
import com.joo.real_world.follow.infrastructure.FollowJpaRepository
import com.joo.real_world.tag.domain.TagRepository
import com.joo.real_world.user.domain.User
import com.joo.real_world.user.domain.vo.Email
import com.joo.real_world.user.domain.vo.Password
import com.joo.real_world.user.infrastructure.UserJpaRepository
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Nested
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
    private val tagRepository: TagRepository,
    private val commentRepository: CommentRepository
) {

    private lateinit var user: User
    private lateinit var anotherUser: User

    @BeforeEach
    fun setup() {
        user = userRepository.save(User(username = "author", email = Email.of("a@test.com"), password = Password.of("pw")))
        anotherUser = userRepository.save(User(username = "follower", email = Email.of("f@test.com"), password = Password.of("pw")))
    }

    @Nested
    @DisplayName("단건 조회")
    inner class FindTest {
        @Test
        @DisplayName("Slug와 UserId로 아티클 단건을 조회할 수 있다")
        fun findBySlugAndUserId() {
            // given
            val slug = "slug-find"
            val article = articleRepository.save(
                Article(
                    slug = Slug(slug),
                    title = Title("title"),
                    description = Description("desc"),
                    body = Body("body"),
                    authorId = anotherUser.id.assertNotNull(),
                    tagIds = emptyList()
                )
            )
            favoriteRepository.save(Favorite(articleId = article, userId = user.id.assertNotNull()))
            followRepository.follow(Follow(followerId = user.id.assertNotNull(), followeeId = anotherUser.id.assertNotNull()))

            // when
            val result = articleQueryRepository.findBySlugAndUserId(slug, user.id.assertNotNull().value)

            // then
            assertThat(result.slug).isEqualTo(slug)
            assertThat(result.favorited).isTrue()
            assertThat(result.author.following).isTrue()
        }

        @Test
        @DisplayName("ArticleId와 UserId로 아티클 단건을 조회할 수 있다")
        fun findByArticleIdAndUserId() {
            // given
            val expectedSlug = "slug-id"
            val articleId = articleRepository.save(
                Article(
                    slug = Slug("slug-id"),
                    title = Title("title"),
                    description = Description("desc"),
                    body = Body("body"),
                    authorId = anotherUser.id.assertNotNull(),
                    tagIds = emptyList()
                )
            )

            // when
            val result = articleQueryRepository.findByArticleIdAndUserId(articleId.assertNotNull().value, user.id.assertNotNull().value)

            // then
            assertThat(result.slug).isEqualTo(expectedSlug)
            assertThat(result.author.username).isEqualTo(anotherUser.username)
        }

        @Test
        @DisplayName("CommentId와 UserId로 댓글 단건을 조회할 수 있다")
        fun findCommentById() {
            // given
            val articleId = articleRepository.save(
                Article(
                    slug = Slug("slug-comment-id"),
                    title = Title("title"),
                    description = Description("desc"),
                    body = Body("body"),
                    authorId = anotherUser.id.assertNotNull(),
                    tagIds = emptyList()
                )
            )

            val commentId = commentRepository.save(
                Comment(
                    articleId = articleId.assertNotNull(),
                    authorId = anotherUser.id.assertNotNull(),
                    body = Body("comment-body")
                )
            )

            // when
            val result = articleQueryRepository.findComment(commentId.value, anotherUser.id.assertNotNull().value)

            // then
            assertThat(result.commentId).isEqualTo(commentId.value)
            assertThat(result.body).isEqualTo("comment-body")
        }
    }
    @Nested
    @DisplayName("findByCondition 조건별 조회")
    inner class FindByConditionTest {

        @Test
        @DisplayName("특정 작성자가 쓴 글을 조회할 수 있다")
        fun findByAuthorId() {
            // given
            val slug1 = "slug-1"
            articleRepository.save(
                Article(
                    slug = Slug(slug1),
                    title = Title("title"),
                    description = Description("desc1"),
                    body = Body("body1"),
                    authorId = anotherUser.id.assertNotNull(),
                    tagIds = emptyList()
                )
            )

            articleRepository.save(
                Article(
                    slug = Slug("slug-2"),
                    title = Title("title2"),
                    description = Description("desc2"),
                    body = Body("body2"),
                    authorId = user.id.assertNotNull(),
                    tagIds = emptyList()
                )
            )

            // when
            val condition = ArticleCondition(userId = user.id.assertNotNull().value, authorId = anotherUser.id.assertNotNull().value)
            val results: List<ArticleDto> = articleQueryRepository.findByCondition(condition, PageSpec())

            // then
            assertThat(results).hasSize(1)
            assertThat(results[0].slug).isEqualTo(slug1)
            assertThat(results[0].author.username).isEqualTo(anotherUser.username)
        }


        @Test
        @DisplayName("특정 태그가 달린 글을 조회할 수 있다")
        fun findByTag() {
            // given
            val tags = tagRepository.findOrCreateTags(listOf("kotlin", "spring"))
            val expectedSlug = "slug1"
            articleRepository.save(
                Article(
                    slug = Slug(expectedSlug),
                    title = Title("title1"),
                    description = Description("desc1"),
                    body = Body("body1"),
                    authorId = user.id.assertNotNull(),
                    tagIds = listOf(tags[0].id.assertNotNull())
                )
            )
            articleRepository.save(
                Article(
                    slug = Slug("slug2"),
                    title = Title("title2"),
                    description = Description("desc2"),
                    body = Body("body2"),
                    authorId = user.id.assertNotNull(),
                    tagIds = listOf(tags[1].id.assertNotNull())
                )
            )

            // when
            val condition = ArticleCondition(userId = user.id.assertNotNull().value, tag = tags[0].name)
            val results: List<ArticleDto> = articleQueryRepository.findByCondition(condition, PageSpec())

            // then
            assertThat(results).hasSize(1)
            assertThat(results[0].slug).isEqualTo(expectedSlug)
            assertThat(results[0].tagList).contains(tags[0].name)
        }

        @Test
        @DisplayName("특정 유저가 좋아요한 글을 조회할 수 있다")
        fun findByFavoriteByUserId() {
            // given
            val expectedSlug = "slug1"
            val articleId1 = articleRepository.save(
                Article(
                    slug = Slug(expectedSlug),
                    title = Title("title"),
                    description = Description("desc1"),
                    body = Body("body1"),
                    authorId = anotherUser.id.assertNotNull(),
                    tagIds = emptyList()
                )
            )

            val articleId2 = articleRepository.save(
                Article(
                    slug = Slug("slug2"),
                    title = Title("title2"),
                    description = Description("desc2"),
                    body = Body("body2"),
                    authorId = user.id.assertNotNull(),
                    tagIds = emptyList()
                )
            )

            favoriteRepository.save(Favorite(articleId = articleId1, userId = user.id.assertNotNull()))

            // when
            val condition = ArticleCondition(userId = user.id.assertNotNull().value, favoriteByUserId = user.id.assertNotNull().value)
            val results: List<ArticleDto> = articleQueryRepository.findByCondition(condition, PageSpec())

            // then
            assertThat(results).hasSize(1)
            assertThat(results[0].slug).isEqualTo(expectedSlug)
            assertThat(results[0].favorited).isTrue()
        }

        @Test
        @DisplayName("좋아요는 했지만 다른 유저로 조회하면 결과가 나오지 않는다")
        fun findByFavoriteByDifferentUser() {
            // given
            val article = articleRepository.save(
                Article(
                    slug = Slug("slug-fav"),
                    title = Title("title"),
                    description = Description("desc"),
                    body = Body("body"),
                    authorId = anotherUser.id.assertNotNull(),
                    tagIds = emptyList()
                )
            )
            favoriteRepository.save(Favorite(articleId = article, userId = user.id.assertNotNull()))

            // when
            val condition = ArticleCondition(userId = anotherUser.id.assertNotNull().value, favoriteByUserId = anotherUser.id.assertNotNull().value)
            val results = articleQueryRepository.findByCondition(condition, PageSpec())

            // then
            assertThat(results).isEmpty()
        }

        @Test
        @DisplayName("조건 없이 전체 글을 조회할 수 있다")
        fun findAll() {
            // given
            articleRepository.save(
                Article(
                    slug = Slug("slug1"),
                    title = Title("title"),
                    description = Description("desc1"),
                    body = Body("body1"),
                    authorId = anotherUser.id.assertNotNull(),
                    tagIds = emptyList()
                )
            )

            articleRepository.save(
                Article(
                    slug = Slug("slug2"),
                    title = Title("title2"),
                    description = Description("desc2"),
                    body = Body("body2"),
                    authorId = user.id.assertNotNull(),
                    tagIds = emptyList()
                )
            )

            // when
            val condition = ArticleCondition(userId = user.id.assertNotNull().value)
            val results: List<ArticleDto> = articleQueryRepository.findByCondition(condition, PageSpec())

            // then
            assertThat(results).hasSize(2)
        }

        @Test
        @DisplayName("PageSpec으로 페이징이 적용된다")
        fun findWithPagination() {
            // given
            (1..5).forEach {
                articleRepository.save(
                    Article(
                        slug = Slug("slug-$it"),
                        title = Title("title$it"),
                        description = Description("desc$it"),
                        body = Body("body$it"),
                        authorId = user.id.assertNotNull(),
                        tagIds = emptyList()
                    )
                )
            }

            // when
            val condition = ArticleCondition(userId = user.id.assertNotNull().value)
            val results = articleQueryRepository.findByCondition(condition, PageSpec(limit = 2, offset = 0))

            // then
            assertThat(results).hasSize(2)
        }

        @Test
        @DisplayName("존재하지 않는 태그 조건이면 빈 리스트를 반환한다")
        fun findByInvalidTag() {
            // given
            articleRepository.save(
                Article(
                    slug = Slug("slug1"),
                    title = Title("title"),
                    description = Description("desc"),
                    body = Body("body"),
                    authorId = user.id.assertNotNull(),
                    tagIds = emptyList()
                )
            )

            // when
            val condition = ArticleCondition(userId = user.id.assertNotNull().value, tag = "non-existent")
            val results = articleQueryRepository.findByCondition(condition, PageSpec())

            // then
            assertThat(results).isEmpty()
        }

    }

    @Nested
    @DisplayName("findFeed 팔로우 조건 조회")
    inner class FindFeedTest {

        @Test
        @DisplayName("유저가 팔로우한 작성자의 글만 조회된다")
        fun findFeedByFollowing() {
            // given
            followRepository.follow(
                Follow(
                    followerId = user.id.assertNotNull(),
                    followeeId = anotherUser.id.assertNotNull()
                )
            )
            val expectedSlug = "slug1"
            articleRepository.save(
                Article(
                    slug = Slug(expectedSlug),
                    title = Title("title"),
                    description = Description("desc1"),
                    body = Body("body1"),
                    authorId = anotherUser.id.assertNotNull(),
                    tagIds = emptyList()
                )
            )

            articleRepository.save(
                Article(
                    slug = Slug("slug2"),
                    title = Title("title2"),
                    description = Description("desc2"),
                    body = Body("body2"),
                    authorId = user.id.assertNotNull(),
                    tagIds = emptyList()
                )
            )

            // when
            val condition = ArticleCondition(userId = user.id.assertNotNull().value)
            val results: List<ArticleDto> = articleQueryRepository.findFeed(condition, PageSpec())

            // then
            assertThat(results).hasSize(1)
            assertThat(results[0].slug).isEqualTo(expectedSlug)
            assertThat(results[0].author.username).isEqualTo(anotherUser.username)
        }

        @Test
        @DisplayName("팔로우하지 않으면 글이 조회되지 않는다")
        fun findFeedWithoutFollow() {
            // given
            articleRepository.save(
                Article(
                    slug = Slug("slug1"),
                    title = Title("title"),
                    description = Description("desc1"),
                    body = Body("body1"),
                    authorId = anotherUser.id.assertNotNull(),
                    tagIds = emptyList()
                )
            )

            articleRepository.save(
                Article(
                    slug = Slug("slug2"),
                    title = Title("title2"),
                    description = Description("desc2"),
                    body = Body("body2"),
                    authorId = user.id.assertNotNull(),
                    tagIds = emptyList()
                )
            )

            // when
            val condition = ArticleCondition(userId = user.id.assertNotNull().value)
            val results: List<ArticleDto> = articleQueryRepository.findFeed(condition, PageSpec())

            // then
            assertThat(results).isEmpty()
        }
    }

    @Test
    @DisplayName("여러 명을 팔로우하면 그들의 글이 모두 조회된다")
    fun findFeedWithMultipleFollowings() {
        val thirdUser = userRepository.save(User(username = "third", email = Email.of("t@test.com"), password = Password.of("pw")))

        followRepository.follow(Follow(followerId = user.id.assertNotNull(), followeeId = anotherUser.id.assertNotNull()))
        followRepository.follow(Follow(followerId = user.id.assertNotNull(), followeeId = thirdUser.id.assertNotNull()))

        val slug1 = "slug-another"
        val slug2 = "slug-third"

        articleRepository.save(
            Article(slug = Slug(slug1), title = Title("t1"), description = Description("d1"), body = Body("b1"), authorId = anotherUser.id.assertNotNull(), tagIds = emptyList())
        )
        articleRepository.save(
            Article(slug = Slug(slug2), title = Title("t2"), description = Description("d2"), body = Body("b2"), authorId = thirdUser.id.assertNotNull(), tagIds = emptyList())
        )

        // when
        val condition = ArticleCondition(userId = user.id.assertNotNull().value)
        val results = articleQueryRepository.findFeed(condition, PageSpec())

        // then
        assertThat(results.map { it.slug }).containsExactlyInAnyOrder(slug1, slug2)
    }

}
