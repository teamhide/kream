package com.teamhide.kream.common.exception

import com.teamhide.kream.common.response.ApiResponse
import com.teamhide.kream.common.response.FailBody
import io.github.oshai.kotlinlogging.KotlinLogging
import io.github.resilience4j.ratelimiter.RequestNotPermitted
import org.springframework.http.converter.HttpMessageNotReadableException
import org.springframework.security.core.AuthenticationException
import org.springframework.web.HttpRequestMethodNotSupportedException
import org.springframework.web.bind.MethodArgumentNotValidException
import org.springframework.web.bind.MissingRequestHeaderException
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.RestControllerAdvice
import org.springframework.web.servlet.NoHandlerFoundException
import org.springframework.web.servlet.resource.NoResourceFoundException

private val logger = KotlinLogging.logger { }

@RestControllerAdvice
class GlobalExceptionHandler {
    @ExceptionHandler(CustomException::class)
    fun handleCustomException(e: CustomException): ApiResponse<FailBody> {
        val body = FailBody(errorCode = e.errorCode, message = e.message)
        return ApiResponse.fail(body = body, statusCode = e.statusCode)
    }

    @ExceptionHandler(HttpMessageNotReadableException::class)
    fun handleHttpMessageNotReadableException(e: HttpMessageNotReadableException): ApiResponse<FailBody> {
        val errorConst = CommonErrorConst.HTTP_MESSAGE_NOT_READABLE
        val body = FailBody(errorCode = errorConst.errorCode, message = errorConst.message)
        return ApiResponse.fail(body, errorConst.statusCode)
    }

    @ExceptionHandler(HttpRequestMethodNotSupportedException::class)
    fun handleHttpRequestMethodNotSupportedException(e: HttpRequestMethodNotSupportedException): ApiResponse<FailBody> {
        val errorConst = CommonErrorConst.HTTP_REQUEST_METHOD_NOT_SUPPORTED
        val body = FailBody(errorCode = errorConst.errorCode, message = errorConst.message)
        return ApiResponse.fail(body, errorConst.statusCode)
    }

    @ExceptionHandler(MethodArgumentNotValidException::class)
    fun handleMethodArgumentNotValidException(e: MethodArgumentNotValidException): ApiResponse<FailBody> {
        val errorConst = CommonErrorConst.METHOD_ARGUMENT_NOT_VALID
        val body = FailBody(errorCode = errorConst.errorCode, message = errorConst.message)
        return ApiResponse.fail(body, errorConst.statusCode)
    }

    @ExceptionHandler(NoHandlerFoundException::class)
    fun handleNoHandlerFoundException(e: NoHandlerFoundException): ApiResponse<FailBody> {
        val errorConst = CommonErrorConst.NO_HANDLER_FOUND
        val body = FailBody(errorCode = errorConst.errorCode, message = errorConst.message)
        return ApiResponse.fail(body, errorConst.statusCode)
    }

    @ExceptionHandler(MissingRequestHeaderException::class)
    fun handleMissingRequestHeaderException(e: MissingRequestHeaderException): ApiResponse<FailBody> {
        val errorConst = CommonErrorConst.MISSING_REQUEST_HEADER
        val body = FailBody(errorCode = errorConst.errorCode, message = errorConst.message)
        return ApiResponse.fail(body, errorConst.statusCode)
    }

    @ExceptionHandler(AuthenticationException::class)
    fun handleAuthenticationException(e: AuthenticationException): ApiResponse<FailBody> {
        val errorConst = CommonErrorConst.AUTHENTICATION_ERROR
        val body = FailBody(errorCode = errorConst.errorCode, message = errorConst.message)
        return ApiResponse.fail(body, errorConst.statusCode)
    }

    @ExceptionHandler(NoResourceFoundException::class)
    fun handleNoResourceFoundException(e: NoResourceFoundException): ApiResponse<FailBody> {
        val errorConst = CommonErrorConst.AUTHENTICATION_ERROR
        val body = FailBody(errorCode = errorConst.errorCode, message = errorConst.message)
        return ApiResponse.fail(body, errorConst.statusCode)
    }

    @ExceptionHandler(RequestNotPermitted::class)
    fun handleRequestNotPermittedException(e: RequestNotPermitted): ApiResponse<FailBody> {
        val errorConst = CommonErrorConst.REQUEST_NOT_PERMITTED
        val body = FailBody(errorCode = errorConst.errorCode, message = errorConst.message)
        return ApiResponse.fail(body, errorConst.statusCode)
    }

    @ExceptionHandler(Exception::class)
    fun handleException(e: Exception): ApiResponse<FailBody> {
        val errorConst = CommonErrorConst.UNKNOWN
        val body = FailBody(errorCode = errorConst.errorCode, message = errorConst.message)
        logger.error { "GlobalExceptionHandler | Unhandled exception $e" }
        return ApiResponse.fail(body, errorConst.statusCode)
    }
}
