package com.joo.real_world.article.domain

import com.joo.real_world.article.domain.vo.ArticleId
import com.joo.real_world.article.domain.vo.FavoriteId
import com.joo.real_world.user.domain.vo.UserId

class Favorite(
    val id: FavoriteId? = null,
    val articleId: ArticleId,
    val userId: UserId
)