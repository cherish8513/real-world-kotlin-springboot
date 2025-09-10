package com.joo.real_world.user.domain.vo

import com.joo.real_world.common.exception.CustomExceptionType

@JvmInline
value class Email private constructor(val value: String) {
    companion object {
        private val EMAIL_REGEX =
            Regex("^[a-zA-Z0-9.!#$%&'*+/=?^_`{|}~-]+@[a-zA-Z0-9](?:[a-zA-Z0-9-]{0,61}[a-zA-Z0-9])?(?:\\.[a-zA-Z0-9](?:[a-zA-Z0-9-]{0,61}[a-zA-Z0-9])?)*$")

        fun of(value: String): Email {
            require(value.isNotBlank() && EMAIL_REGEX.matches(value)) {
                throw CustomExceptionType.INVALID_EMAIL_FORMAT.toException()
            }
            return Email(value)
        }
    }

    override fun toString(): String = value
}