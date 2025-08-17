package com.joo.real_world.user.presentation

data class RegisterRequest(
    val username: String,
    val email: String,
    val password: String,
)