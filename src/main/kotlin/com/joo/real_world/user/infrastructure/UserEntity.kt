package com.joo.real_world.user.infrastructure

import jakarta.persistence.*

@Entity
@Table(name = "users")
class UserEntity(
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long?,
    val username: String,
    val email: String,
    val password: String,
    val bio: String?,
    val image: String?,
)