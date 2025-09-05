package com.joo.real_world.tag.domain.vo

@JvmInline
value class TagId(val value: Long) {
    override fun toString(): String = value.toString()
}