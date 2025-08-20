package com.joo.real_world.user.infrastructure

import jakarta.persistence.Entity
import jakarta.persistence.Id
import jakarta.persistence.IdClass
import jakarta.persistence.Table
import java.io.Serializable


@IdClass(FollowerId::class)
@Entity
@Table(name = "followers")
class FollowerEntity(
    @Id
    val followerId: Long,
    @Id
    val followeeId: Long
)

data class FollowerId(
    val followerId: Long,
    val followeeId: Long
) : Serializable
