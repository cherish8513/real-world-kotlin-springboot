package com.joo.real_world.user.infrastructure

import com.joo.real_world.common.util.assertNotNull
import com.joo.real_world.user.domain.User
import com.joo.real_world.user.domain.vo.Email
import com.joo.real_world.user.domain.vo.Password
import com.joo.real_world.user.domain.vo.UserId

object UserMapper {
    fun toEntity(user: User): UserEntity =
        UserEntity(
            id = user.id?.value,
            username = user.username,
            email = user.email.value,
            password = user.password.value,
            bio = user.bio,
            image = user.image,
        )

    fun toDomain(entity: UserEntity): User =
        User(
            id = UserId(entity.id.assertNotNull()),
            username = entity.username,
            email = Email.of(entity.email),
            password = Password.of(entity.password),
            bio = entity.bio,
            image = entity.image
        )
}