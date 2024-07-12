package com.teamhide.kream.common.filter

import io.github.oshai.kotlinlogging.KotlinLogging
import jakarta.servlet.FilterChain
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.springframework.stereotype.Component
import org.springframework.util.StreamUtils
import org.springframework.web.filter.OncePerRequestFilter
import org.springframework.web.util.ContentCachingRequestWrapper
import org.springframework.web.util.ContentCachingResponseWrapper
import java.io.ByteArrayInputStream
import java.lang.Exception
import java.nio.charset.StandardCharsets

private val logger = KotlinLogging.logger { }

@Component
class RequestLogFilter : OncePerRequestFilter() {
    override fun doFilterInternal(
        request: HttpServletRequest,
        response: HttpServletResponse,
        filterChain: FilterChain,
    ) {
        val cachedRequest = ContentCachingRequestWrapper(request)
        val cachedResponse = ContentCachingResponseWrapper(response)

        filterChain.doFilter(cachedRequest, cachedResponse)
        logRequest(request = cachedRequest)
        logResponse(request = cachedRequest, response = cachedResponse)
    }

    companion object {
        fun logRequest(request: ContentCachingRequestWrapper) {
            val method = request.method
            val requestURI = request.requestURI
            val queryString = request.queryString
            val formattedQueryString = queryString?.takeIf { it.isNotEmpty() }?.let { "?$it" } ?: ""
            val requestBody = getRequestBody(request)
            val logMessage = buildString {
                append("Request | $method $requestURI$formattedQueryString")
                if (requestBody.isNotEmpty()) {
                    append(" | body = $requestBody")
                }
            }

            logger.info { logMessage }
        }

        fun logResponse(request: ContentCachingRequestWrapper, response: ContentCachingResponseWrapper) {
            val method = request.method
            val requestURI = request.requestURI
            val queryString = request.queryString
            val formattedQueryString = queryString?.takeIf { it.isNotEmpty() }?.let { "?$it" } ?: ""
            val statusCode = response.status
            val responseBody = getResponseBody(response)
            val logMessage = buildString {
                append("Response | $method $requestURI$formattedQueryString | $statusCode")
                if (responseBody.isNotEmpty()) {
                    append(" | body = $responseBody")
                }
            }

            if (statusCode < 500) {
                logger.info { logMessage }
            } else {
                logger.error { logMessage }
            }
        }

        fun getRequestBody(request: ContentCachingRequestWrapper): String {
            return try {
                ByteArrayInputStream(request.contentAsByteArray).use { inputStream ->
                    StreamUtils.copyToString(inputStream, StandardCharsets.UTF_8)
                }
            } catch (e: Exception) {
                ""
            }
        }

        fun getResponseBody(response: ContentCachingResponseWrapper): String {
            return try {
                response.contentInputStream.use { inputStream ->
                    val responseBody = StreamUtils.copyToString(inputStream, StandardCharsets.UTF_8)
                    response.copyBodyToResponse()
                    responseBody
                }
            } catch (e: Exception) {
                ""
            }
        }
    }
}
