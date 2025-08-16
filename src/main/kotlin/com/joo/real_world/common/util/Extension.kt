package com.joo.real_world.common.util

import com.joo.real_world.common.exception.CustomExceptionType

fun <T> T?.assertNotNull(customExceptionType: CustomExceptionType? = null): T {
    return this ?: throw customExceptionType?.toException() ?: CustomExceptionType.UNEXPECTED_ERROR_OCCURRED.toException("expected not null but null")
}