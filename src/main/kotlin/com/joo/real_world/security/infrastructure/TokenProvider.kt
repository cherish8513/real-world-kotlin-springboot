package com.joo.real_world.security.infrastructure

interface TokenProvider {
    fun generateToken(userId: Long, username: String, email: String): String
    fun extractUserId(token: String): Long
    fun isTokenValid(token: String): Boolean

}