package com.joo.real_world.user.application

import com.joo.real_world.common.util.assertNotNull
import com.joo.real_world.user.domain.User

fun User.toUserDto() =
    UserDto(
        id = this.id.assertNotNull().value,
        username = this.username,
        email = this.email.value,
        bio = this.bio,
        image = this.image
    )