package com.joo.real_world.user.domain

import com.joo.real_world.user.domain.value.Email
import com.joo.real_world.user.domain.value.Password
import com.joo.real_world.user.domain.value.UserId

class User (
    val id: UserId? = null,
    val username: String,
    val email: Email,
    val password: Password
)