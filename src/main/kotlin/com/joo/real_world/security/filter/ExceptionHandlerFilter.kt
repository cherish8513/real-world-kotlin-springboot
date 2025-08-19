package com.joo.real_world.security.filter

import com.joo.real_world.common.exception.CustomException
import com.joo.real_world.common.exception.CustomExceptionType
import jakarta.servlet.FilterChain
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.springframework.stereotype.Component
import org.springframework.web.filter.OncePerRequestFilter

@Component
class ExceptionHandlerFilter : OncePerRequestFilter() {

    override fun doFilterInternal(
        request: HttpServletRequest,
        response: HttpServletResponse,
        filterChain: FilterChain
    ) {
        try {
            filterChain.doFilter(request, response)
        } catch (e: Exception) {
            handleFilterException(response, e)
        }
    }

    private fun handleFilterException(response: HttpServletResponse, e: Exception) {
        val (status, message) = when (e) {
            is CustomException -> e.exceptionType.httpStatus.value() to (e.message ?: e.exceptionType.message)
            else -> CustomExceptionType.UNEXPECTED_ERROR_OCCURRED.httpStatus.value() to
                    (e.message ?: CustomExceptionType.UNEXPECTED_ERROR_OCCURRED.message)
        }

        // JSON 응답
        val jsonResponse = """
            {
              "errors": {
                "body": [
                  "${message.replace("\"", "\\\"")}"
                ]
              }
            }
        """.trimIndent()

        response.status = status
        response.contentType = "application/json"
        response.characterEncoding = "UTF-8"
        response.writer.write(jsonResponse)
        response.writer.flush()
    }
}
