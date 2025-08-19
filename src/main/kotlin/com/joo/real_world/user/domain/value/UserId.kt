package com.joo.real_world.user.domain.value

@JvmInline
value class UserId(val value: Long) {
    override fun toString(): String = value.toString()
}