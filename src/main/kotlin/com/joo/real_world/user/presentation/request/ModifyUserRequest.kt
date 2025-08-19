package com.joo.real_world.user.presentation.request

import com.fasterxml.jackson.annotation.JsonProperty
import jakarta.validation.Valid
import jakarta.validation.constraints.Email
import jakarta.validation.constraints.Size

data class ModifyUserRequest(
    @field:Valid
    @field:JsonProperty("user")
    val modifyUser: ModifyUser
)

data class ModifyUser(
    @field:Email(message = "Invalid email format")
    val email: String? = null,
    @field:Size(min = 1, message = "Username must not be blank")
    val username: String? = null,
    @field:Size(min = 1, message = "Password must not be blank")
    val password: String? = null,
    val bio: String? = null,
    val image: String? = null,
)
