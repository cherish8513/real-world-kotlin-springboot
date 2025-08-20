package com.joo.real_world.user.domain

import com.joo.real_world.user.domain.vo.Email
import com.joo.real_world.user.domain.vo.Password
import com.joo.real_world.user.domain.vo.UserId

class User(
    val id: UserId? = null,
    val email: Email,
    val username: String,
    val password: Password,
    val bio: String? = null,
    val image: String? = null,
) {
    fun change(
        email: String?,
        username: String?,
        password: Password?,
        bio: String?,
        image: String?
    ): User {
        return User(
            id = id,
            email = email?.let { Email.of(it) } ?: this.email,
            username = username ?: this.username,
            password = password ?: this.password,
            bio = bio ?: this.bio,
            image = image ?: this.image
        )
    }
}