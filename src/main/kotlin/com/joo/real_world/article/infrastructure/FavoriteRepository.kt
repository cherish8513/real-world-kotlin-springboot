package com.joo.real_world.article.infrastructure

import com.joo.real_world.article.domain.Favorite
import com.joo.real_world.article.domain.vo.FavoriteId
import com.joo.real_world.common.util.assertNotNull
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

interface FavoriteRepository {
    fun save(favorite: Favorite): FavoriteId
}

interface IFavoriteJpaRepository : JpaRepository<FavoriteEntity, Long> {
    fun countByArticleId(articleId: Long): Int
    fun existsByArticleIdAndUserId(articleId: Long, userId: Long): Boolean
}

@Repository
class FavoriteJpaRepositoryImpl(
    private val favoriteJpaRepository: IFavoriteJpaRepository
) : FavoriteRepository {
    override fun save(favorite: Favorite): FavoriteId {
        return favoriteJpaRepository.save(
            FavoriteEntity(
                userId = favorite.userId.value,
                articleId = favorite.articleId.value
            )
        ).let { FavoriteId(it.id.assertNotNull()) }
    }

}