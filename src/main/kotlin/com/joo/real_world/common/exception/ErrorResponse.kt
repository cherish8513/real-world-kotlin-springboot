package com.joo.real_world.common.exception

import com.fasterxml.jackson.annotation.JsonInclude

@JsonInclude(JsonInclude.Include.NON_NULL)
data class ErrorResponse(
    val errors: Errors
)

data class Errors(
    val body: List<String>
)