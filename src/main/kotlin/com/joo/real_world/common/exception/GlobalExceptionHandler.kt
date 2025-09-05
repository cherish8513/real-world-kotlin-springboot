package com.joo.real_world.common.exception

import jakarta.validation.ConstraintViolationException
import org.slf4j.LoggerFactory
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.MethodArgumentNotValidException
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.RestControllerAdvice

@RestControllerAdvice
class GlobalExceptionHandler {
    private val log = LoggerFactory.getLogger(GlobalExceptionHandler::class.java)

    @ExceptionHandler(
        value = [MethodArgumentNotValidException::class, ConstraintViolationException::class]
    )
    fun handleValidationException(e: Exception): ResponseEntity<ErrorResponse> {
        val errorMessages = when (e) {
            is MethodArgumentNotValidException ->
                e.bindingResult.fieldErrors.map { it.defaultMessage ?: "Validation error" }

            is ConstraintViolationException ->
                e.constraintViolations.map { it.message }

            else -> listOf("Validation error")
        }

        val errorResponse = ErrorResponse(
            errors = Errors(
                body = errorMessages
            )
        )

        return ResponseEntity.badRequest().body(errorResponse)
    }


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

    @ExceptionHandler(Exception::class)
    fun handleUnexpectedException(e: Exception): ResponseEntity<ErrorResponse> {
        val errorResponse = ErrorResponse(
            errors = Errors(
                body = listOf(e.message ?: CustomExceptionType.UNEXPECTED_ERROR_OCCURRED.message)
            )
        )

        log.error("Unexpected error occurred", e)

        return ResponseEntity
            .status(HttpStatus.INTERNAL_SERVER_ERROR)
            .body(errorResponse)
    }
}