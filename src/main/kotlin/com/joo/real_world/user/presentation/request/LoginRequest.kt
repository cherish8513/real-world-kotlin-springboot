package com.joo.real_world.user.presentation.request

import com.fasterxml.jackson.annotation.JsonProperty
import jakarta.validation.Valid
import jakarta.validation.constraints.Email
import jakarta.validation.constraints.NotBlank

data class LoginRequest(
    @field:Valid
    @JsonProperty("user")
    val loginUser: LoginUser
)

data class LoginUser(
    @field:NotBlank
    @field:Email
    val email: String,
    @field:NotBlank
    val password: String
)