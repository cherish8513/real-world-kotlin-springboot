package com.joo.real_world.user.infrastructure

import com.joo.real_world.user.domain.User

interface UserRepository {
    fun save(user: User): User
}