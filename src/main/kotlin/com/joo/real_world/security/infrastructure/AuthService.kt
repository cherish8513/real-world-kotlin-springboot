package com.joo.real_world.security.infrastructure

interface AuthService {
    fun login(userId: Long): String
    fun getUserSession(token: String): UserSession
}