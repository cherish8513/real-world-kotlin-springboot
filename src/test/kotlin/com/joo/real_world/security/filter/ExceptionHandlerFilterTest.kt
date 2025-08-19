package com.joo.real_world.security.filter

import com.joo.real_world.common.exception.CustomExceptionType
import jakarta.servlet.ServletRequest
import jakarta.servlet.ServletResponse
import org.junit.jupiter.api.Test
import org.springframework.mock.web.MockFilterChain
import org.springframework.mock.web.MockHttpServletRequest
import org.springframework.mock.web.MockHttpServletResponse
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class ExceptionHandlerFilterTest {

    private val filter = ExceptionHandlerFilter()

    @Test
    fun `runtime exception should return 500 with JSON body`() {
        // given
        val request = MockHttpServletRequest()
        val response = MockHttpServletResponse()
        val filterChain = object : MockFilterChain() {
            override fun doFilter(req: ServletRequest, res: ServletResponse) {
                throw RuntimeException("Something went wrong")
            }
        }

        // when
        filter.doFilter(request, response, filterChain)

        // then
        assertEquals(500, response.status)
        assertEquals("application/json;charset=UTF-8", response.contentType)
        val content = response.contentAsString
        assertTrue(content.contains("Something went wrong"))
        assertTrue(content.contains("errors"))
    }

    @Test
    fun `custom exception should return its own status and message`() {
        // given
        val request = MockHttpServletRequest()
        val response = MockHttpServletResponse()
        val filterChain = object : MockFilterChain() {
            override fun doFilter(req: ServletRequest, res: ServletResponse) {
                throw CustomExceptionType.INVALID_USER.toException()
            }
        }

        // when
        filter.doFilter(request, response, filterChain)

        // then
        assertEquals(CustomExceptionType.INVALID_USER.httpStatus.value(), response.status)
        assertEquals("application/json;charset=UTF-8", response.contentType)
        val content = response.contentAsString
        assertTrue(content.contains(CustomExceptionType.INVALID_USER.message))
        assertTrue(content.contains("errors"))
    }

    @Test
    fun `no exception should pass filter chain normally`() {
        // given
        val request = MockHttpServletRequest()
        val response = MockHttpServletResponse()
        val filterChain = MockFilterChain()

        // when
        filter.doFilter(request, response, filterChain)

        // then
        // status 200, 아무 내용 없음
        assertEquals(200, response.status)
        assertEquals("", response.contentAsString)
    }
}
