package com.joo.real_world.common.exception

import org.springframework.http.HttpStatus

class CustomException(val exceptionType: CustomExceptionType, message: String?) : RuntimeException(message)

enum class CustomExceptionType(
    val httpStatus: HttpStatus,
    val message: String = "알 수 없는 에러가 발생했습니다."
) {
    UNEXPECTED_ERROR_OCCURRED(HttpStatus.INTERNAL_SERVER_ERROR, "알 수 없는 에러가 발생했습니다."),
    LOGIN_REQUIRED(HttpStatus.UNAUTHORIZED, "로그인 후 이용할 수 있습니다.");

    fun toException(message: String? = null): CustomException {
        return CustomException(this, message ?: this.message)
    }
}