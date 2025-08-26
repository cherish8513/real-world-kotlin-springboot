package com.joo.real_world.common.exception

import org.springframework.http.HttpStatus

class CustomException(val exceptionType: CustomExceptionType, message: String?) : RuntimeException(message)

enum class CustomExceptionType(
    val httpStatus: HttpStatus,
    val message: String = "알 수 없는 에러가 발생했습니다."
) {
    UNEXPECTED_ERROR_OCCURRED(HttpStatus.INTERNAL_SERVER_ERROR, "알 수 없는 에러가 발생했습니다."),
    BAD_REQUEST(HttpStatus.BAD_REQUEST, "잘못된 요청입니다."),
    DUPLICATE_EMAIL_EXIST(HttpStatus.FORBIDDEN, "중복된 이메일이 존재합니다."),
    DUPLICATE_NAME_EXIST(HttpStatus.FORBIDDEN, "중복된 이름이 존재합니다."),
    INVALID_USER(HttpStatus.NOT_FOUND, "존재하지 않는 유저 입니다."),
    PASSWORD_INCORRECT(HttpStatus.NOT_FOUND, "비밀번호가 맞지 않습니다."),
    LOGIN_REQUIRED(HttpStatus.UNAUTHORIZED, "로그인 후 이용할 수 있습니다."),
    INVALID_EMAIL_FORMAT(HttpStatus.BAD_REQUEST, "유효하지 않은 이메일 형식입니다."),
    ALREADY_FOLLOW(HttpStatus.BAD_REQUEST, "이미 팔로우 되어 있습니다."),
    NOT_FOLLOW(HttpStatus.BAD_REQUEST, "팔로우가 되어 있지 않습니다."),
    SELF_FOLLOW_NOT_ALLOWED(HttpStatus.BAD_REQUEST, "자기 자신을 팔로우 할 수 없습니다."),
    NOT_FOUND_SLUG(HttpStatus.NOT_FOUND, "존재하지 않는 slug 입니다.");


    fun toException(message: String? = null): CustomException {
        return CustomException(this, message ?: this.message)
    }
}