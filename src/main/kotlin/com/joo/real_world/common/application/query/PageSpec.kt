package com.joo.real_world.common.application.query

data class PageSpec(
    val limit: Long = 20,
    val offset: Long = 0
)