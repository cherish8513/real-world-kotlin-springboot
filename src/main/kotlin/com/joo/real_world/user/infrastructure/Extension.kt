package com.joo.real_world.user.infrastructure

import com.joo.real_world.common.util.assertNotNull
import com.joo.real_world.user.domain.Follow
import com.joo.real_world.user.domain.User
import com.joo.real_world.user.domain.vo.Email
import com.joo.real_world.user.domain.vo.Password
import com.joo.real_world.user.domain.vo.UserId

fun User.toEntity(): UserEntity =
    UserEntity(
        id = this.id?.value,
        username = this.username,
        email = this.email.value,
        password = this.password.value,
        bio = this.bio,
        image = this.image,
    )

fun UserEntity.toDomain(): User =
    User(
        id = UserId(this.id.assertNotNull()),
        username = this.username,
        email = Email.of(this.email),
        password = Password.of(this.password),
        bio = this.bio,
        image = this.image
    )

fun FollowEntity.toDomain(): Follow =
    Follow(
        followerId = UserId(this.followerId),
        followeeId = UserId(this.followeeId)
    )

fun Follow.toEntity(): FollowEntity =
    FollowEntity(
        FollowerId(
            followerId = this.followerId.value,
            followeeId = this.followeeId.value
        )
    )