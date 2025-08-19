package com.joo.real_world.user.domain.vo

import com.joo.real_world.common.exception.CustomExceptionType

@JvmInline
value class Email private constructor(val value: String) {
    companion object {
        // RFC 5322 기반의 실용적인 이메일 정규식 (Jakarta @Email과 유사)
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