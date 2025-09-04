package com.joo.real_world.article.domain.vo

@JvmInline
value class CommentId(val value: Long) {
    override fun toString(): String = value.toString()
}