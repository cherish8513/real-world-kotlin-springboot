package com.joo.real_world.user.domain

import com.joo.real_world.user.domain.vo.Email
import com.joo.real_world.user.domain.vo.UserId

interface UserRepository {
    fun save(user: User): User
    fun findByEmail(email: Email): User?
    fun findByUserId(userId: UserId): User?
    fun findByUsername(username: String): User?
}