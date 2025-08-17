package com.joo.real_world.user.domain

import com.joo.real_world.user.domain.value.Email
import com.joo.real_world.user.domain.value.UserId

interface UserRepository {
    fun save(user: User): User
    fun findByEmail(email: Email): User?
    fun findByUserId(userId: UserId): User?
}