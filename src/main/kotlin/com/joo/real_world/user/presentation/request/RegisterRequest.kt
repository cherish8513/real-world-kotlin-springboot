package com.joo.real_world.user.presentation.request

import com.fasterxml.jackson.annotation.JsonProperty
import jakarta.validation.Valid
import jakarta.validation.constraints.Email
import jakarta.validation.constraints.NotBlank

data class RegisterRequest(
    @field:Valid
    @field:JsonProperty("user")
    val registerUser: RegisterUser
)


data class RegisterUser(
    @field:NotBlank
    val username: String,
    @field:NotBlank
    @field:Email
    val email: String,
    @field:NotBlank
    val password: String,
)