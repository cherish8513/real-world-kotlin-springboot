package com.joo.real_world.article.domain.vo

@JvmInline
value class FavoriteId(val value: Long) {
    override fun toString(): String = value.toString()
}