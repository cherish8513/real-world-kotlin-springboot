package com.joo.real_world.common.exception

import com.fasterxml.jackson.annotation.JsonInclude
import java.time.LocalDateTime

@JsonInclude(JsonInclude.Include.NON_NULL)
data class ErrorResponse(
    val success: Boolean = false,
    val message: String,
    val errorCode: String? = null,
    val path: String? = null,
    val details: Any? = null
)