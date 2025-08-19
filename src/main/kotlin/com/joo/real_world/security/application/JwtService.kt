package com.joo.real_world.security.application

interface JwtService {
    fun generateToken(userId: Long, username: String, email: String): String
    fun extractUserId(token: String): Long
    fun isTokenValid(token: String): Boolean

}