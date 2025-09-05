package com.joo.real_world.tag.infrastructure

import jakarta.persistence.*

@Entity
@Table(name = "tag")
class TagEntity(
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long? = null,

    @Column(unique = true, nullable = false)
    val name: String
)
