package com.joo.real_world.user.domain

import com.joo.real_world.user.domain.value.Email
import com.joo.real_world.user.domain.value.Password
import com.joo.real_world.user.domain.value.UserId

class User(
    val id: UserId? = null,
    val email: Email,
    val username: String,
    val password: Password,
    val bio: String? = null,
    val image: String? = null,
)