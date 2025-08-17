package com.joo.real_world.user.domain.value

@JvmInline
value class Email private constructor(val value: String) {
    companion object {
        fun of(value: String): Email {
            require(Regex("^[A-Za-z0-9+_.-]+@(.+)$").matches(value)) {
                "Invalid email format"
            }
            return Email(value)
        }
    }
}