package com.joo.real_world.user.domain.value

import org.springframework.security.crypto.password.PasswordEncoder

@JvmInline
value class Password private constructor(val value: String) {
    fun matches(raw: String, encoder: PasswordEncoder): Boolean =
        encoder.matches(raw, value)

    companion object {
        fun of(encoded: String): Password = Password(encoded)
    }

    override fun toString() = value
}