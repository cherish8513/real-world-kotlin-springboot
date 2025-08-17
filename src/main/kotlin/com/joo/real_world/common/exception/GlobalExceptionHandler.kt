package com.joo.real_world.common.exception

import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.MethodArgumentNotValidException
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.RestControllerAdvice

@RestControllerAdvice
class GlobalExceptionHandler {

    @ExceptionHandler(CustomException::class)
    fun handleCustomException(e: CustomException): ResponseEntity<ErrorResponse> {
        val errorResponse = ErrorResponse(
            errors = Errors(
                body = listOf(e.message ?: CustomExceptionType.UNEXPECTED_ERROR_OCCURRED.message)
            )
        )

        return ResponseEntity
            .status(e.exceptionType.httpStatus)
            .body(errorResponse)
    }

    @ExceptionHandler(MethodArgumentNotValidException::class)
    fun handleValidationException(e: MethodArgumentNotValidException): ResponseEntity<ErrorResponse> {
        val errorMessages = e.bindingResult.fieldErrors.map { it.defaultMessage ?: "Validation error" }

        val errorResponse = ErrorResponse(
            errors = Errors(
                body = errorMessages
            )
        )

        return ResponseEntity.badRequest().body(errorResponse)
    }

    @ExceptionHandler(Exception::class)
    fun handleUnexpectedException(e: Exception): ResponseEntity<ErrorResponse> {
        val errorResponse = ErrorResponse(
            errors = Errors(
                body = listOf(e.message ?: CustomExceptionType.UNEXPECTED_ERROR_OCCURRED.message)
            )
        )

        return ResponseEntity
            .status(HttpStatus.INTERNAL_SERVER_ERROR)
            .body(errorResponse)
    }
}