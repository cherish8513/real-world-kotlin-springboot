package com.joo.real_world.common.exception

import jakarta.servlet.http.HttpServletRequest
import org.slf4j.LoggerFactory
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.validation.BindException
import org.springframework.web.bind.MethodArgumentNotValidException
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.RestControllerAdvice

@RestControllerAdvice
class GlobalExceptionHandler {

    private val logger = LoggerFactory.getLogger(GlobalExceptionHandler::class.java)

    @ExceptionHandler(CustomException::class)
    fun handleCustomException(
        ex: CustomException,
        request: HttpServletRequest
    ): ResponseEntity<ErrorResponse> {
        logger.warn("CustomException [{}]: {}", ex.exceptionType.name, ex.message, ex)

        val errorResponse = ErrorResponse(
            message = ex.message ?: ex.exceptionType.message,
            errorCode = ex.exceptionType.name,
            path = request.requestURI
        )

        return ResponseEntity(errorResponse, ex.exceptionType.httpStatus)
    }

    @ExceptionHandler(MethodArgumentNotValidException::class)
    fun handleValidationException(
        ex: MethodArgumentNotValidException,
        request: HttpServletRequest
    ): ResponseEntity<ErrorResponse> {
        logger.warn("Validation error: {}", ex.message)

        // 첫 번째 에러의 메시지를 메인 메시지로 사용
        val firstError = ex.bindingResult.fieldErrors.firstOrNull()
        val mainMessage = firstError?.defaultMessage ?: "입력값이 올바르지 않습니다."

        // 모든 필드 에러 정보 수집
        val fieldErrors = ex.bindingResult.fieldErrors.map { error ->
            mapOf(
                "field" to error.field,
                "rejectedValue" to (error.rejectedValue?.toString() ?: "null"),
                "message" to (error.defaultMessage ?: "validation error"),
                "code" to error.code
            )
        }

        val globalErrors = ex.bindingResult.globalErrors.map { error ->
            mapOf(
                "objectName" to error.objectName,
                "message" to (error.defaultMessage ?: "validation error"),
                "code" to error.code
            )
        }

        val allErrors = mutableMapOf<String, Any>()
        if (fieldErrors.isNotEmpty()) allErrors["fieldErrors"] = fieldErrors
        if (globalErrors.isNotEmpty()) allErrors["globalErrors"] = globalErrors

        val errorResponse = ErrorResponse(
            message = mainMessage,
            errorCode = "VALIDATION_ERROR",
            path = request.requestURI,
            details = allErrors
        )

        return ResponseEntity(errorResponse, HttpStatus.BAD_REQUEST)
    }

    @ExceptionHandler(BindException::class)
    fun handleBindException(
        ex: BindException,
        request: HttpServletRequest
    ): ResponseEntity<ErrorResponse> {
        logger.warn("Bind error: {}", ex.message)

        val firstError = ex.bindingResult.fieldErrors.firstOrNull()
        val mainMessage = firstError?.defaultMessage ?: "요청 파라미터가 올바르지 않습니다."

        val fieldErrors = ex.bindingResult.fieldErrors.map { error ->
            mapOf(
                "field" to error.field,
                "rejectedValue" to (error.rejectedValue?.toString() ?: "null"),
                "message" to (error.defaultMessage ?: "validation error"),
                "code" to error.code
            )
        }

        val errorResponse = ErrorResponse(
            message = mainMessage,
            errorCode = "BIND_ERROR",
            path = request.requestURI,
            details = mapOf("fieldErrors" to fieldErrors)
        )

        return ResponseEntity(errorResponse, HttpStatus.BAD_REQUEST)
    }
    @ExceptionHandler(Exception::class)
    fun handleGeneralException(
        ex: Exception,
        request: HttpServletRequest
    ): ResponseEntity<ErrorResponse> {
        logger.error("Unexpected error occurred", ex)

        val errorResponse = ErrorResponse(
            message = CustomExceptionType.UNEXPECTED_ERROR_OCCURRED.message,
            errorCode = CustomExceptionType.UNEXPECTED_ERROR_OCCURRED.name,
            path = request.requestURI
        )

        return ResponseEntity(errorResponse, HttpStatus.INTERNAL_SERVER_ERROR)
    }
}
