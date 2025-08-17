package com.joo.real_world.user.application.service

import com.joo.real_world.user.domain.User

interface UserService {
    fun register(username: String, email: String, password: String)
    fun getUser(userId: String): User
}