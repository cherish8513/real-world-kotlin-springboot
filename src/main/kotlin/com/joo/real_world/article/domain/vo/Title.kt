package com.joo.real_world.article.domain.vo

@JvmInline
value class Title(val value: Long) {
    override fun toString(): String = value.toString()
}