package com.teamhide.kream.common.exception

import com.teamhide.kream.common.security.JwtAuthenticationFailException
import io.kotest.core.spec.style.BehaviorSpec
import io.kotest.matchers.shouldBe
import io.mockk.mockk
import org.springframework.core.MethodParameter
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpInputMessage
import org.springframework.http.HttpMethod
import org.springframework.http.HttpStatus
import org.springframework.http.converter.HttpMessageNotReadableException
import org.springframework.validation.BindingResult
import org.springframework.web.HttpRequestMethodNotSupportedException
import org.springframework.web.bind.MethodArgumentNotValidException
import org.springframework.web.bind.MissingRequestHeaderException
import org.springframework.web.servlet.NoHandlerFoundException
import org.springframework.web.servlet.resource.NoResourceFoundException
import java.lang.Exception

internal class GlobalExceptionHandlerTest : BehaviorSpec({
    given("CustomException이 발생하는 경우") {
        class TestException : CustomException(statusCode = HttpStatus.NOT_FOUND, errorCode = "NOT_FOUND", message = "message")
        val exc = TestException()
        val handler = GlobalExceptionHandler()

        `when`("예외 핸들러가 동작하면") {
            val sut = handler.handleCustomException(e = exc)

            then("FailBody로 감싸서 반환된다") {
                sut.body?.errorCode shouldBe exc.errorCode
                sut.body?.message shouldBe exc.message
                sut.statusCode shouldBe exc.statusCode
            }
        }
    }

    given("HttpMessageNotReadableException이 발생하는 경우") {
        val mockHttpInputMessage = mockk<HttpInputMessage>()
        val exc = HttpMessageNotReadableException("message", mockHttpInputMessage)
        val handler = GlobalExceptionHandler()
        val errorConst = CommonErrorConst.HTTP_MESSAGE_NOT_READABLE

        `when`("예외 핸들러가 동작하면") {
            val sut = handler.handleHttpMessageNotReadableException(e = exc)

            then("FailBody로 감싸서 반환된다") {
                sut.body?.errorCode shouldBe errorConst.errorCode
                sut.body?.message shouldBe errorConst.message
                sut.statusCode shouldBe errorConst.statusCode
            }
        }
    }

    given("HttpRequestMethodNotSupportedException 발생하는 경우") {
        val exc = HttpRequestMethodNotSupportedException("POST")
        val handler = GlobalExceptionHandler()
        val errorConst = CommonErrorConst.HTTP_REQUEST_METHOD_NOT_SUPPORTED

        `when`("예외 핸들러가 동작하면") {
            val sut = handler.handleHttpRequestMethodNotSupportedException(e = exc)

            then("FailBody로 감싸서 반환된다") {
                sut.body?.errorCode shouldBe errorConst.errorCode
                sut.body?.message shouldBe errorConst.message
                sut.statusCode shouldBe errorConst.statusCode
            }
        }
    }

    given("MethodArgumentNotValidException 발생하는 경우") {
        val mockMethodParameter = mockk<MethodParameter>()
        val mockBindingResult = mockk<BindingResult>()
        val exc = MethodArgumentNotValidException(mockMethodParameter, mockBindingResult)
        val handler = GlobalExceptionHandler()
        val errorConst = CommonErrorConst.METHOD_ARGUMENT_NOT_VALID

        `when`("예외 핸들러가 동작하면") {
            val sut = handler.handleMethodArgumentNotValidException(e = exc)

            then("FailBody로 감싸서 반환된다") {
                sut.body?.errorCode shouldBe errorConst.errorCode
                sut.body?.message shouldBe errorConst.message
                sut.statusCode shouldBe errorConst.statusCode
            }
        }
    }

    given("NoHandlerFoundException 발생하는 경우") {
        val mockHeader = mockk<HttpHeaders>()
        val exc = NoHandlerFoundException("POST", "URL", mockHeader)
        val handler = GlobalExceptionHandler()
        val errorConst = CommonErrorConst.NO_HANDLER_FOUND

        `when`("예외 핸들러가 동작하면") {
            val sut = handler.handleNoHandlerFoundException(e = exc)

            then("FailBody로 감싸서 반환된다") {
                sut.body?.errorCode shouldBe errorConst.errorCode
                sut.body?.message shouldBe errorConst.message
                sut.statusCode shouldBe errorConst.statusCode
            }
        }
    }

    given("MissingRequestHeaderException 발생하는 경우") {
        val mockMethodParameter = mockk<MethodParameter>()
        val exc = MissingRequestHeaderException("X-API-Key", mockMethodParameter)
        val handler = GlobalExceptionHandler()
        val errorConst = CommonErrorConst.MISSING_REQUEST_HEADER

        `when`("예외 핸들러가 동작하면") {
            val sut = handler.handleMissingRequestHeaderException(e = exc)

            then("FailBody로 감싸서 반환된다") {
                sut.body?.errorCode shouldBe errorConst.errorCode
                sut.body?.message shouldBe errorConst.message
                sut.statusCode shouldBe errorConst.statusCode
            }
        }
    }

    given("Exception 발생하는 경우") {
        val exc = Exception()
        val handler = GlobalExceptionHandler()
        val errorConst = CommonErrorConst.UNKNOWN

        `when`("예외 핸들러가 동작하면") {
            val sut = handler.handleException(e = exc)

            then("FailBody로 감싸서 반환된다") {
                sut.body?.errorCode shouldBe errorConst.errorCode
                sut.body?.message shouldBe errorConst.message
                sut.statusCode shouldBe errorConst.statusCode
            }
        }
    }

    given("AuthenticationException이 발생하는 경우") {
        val exc = JwtAuthenticationFailException()
        val handler = GlobalExceptionHandler()
        val errorConst = CommonErrorConst.AUTHENTICATION_ERROR

        `when`("예외 핸들러가 동작하면") {
            val sut = handler.handleAuthenticationException(e = exc)

            then("FailBody로 감싸서 반환된다") {
                sut.body?.errorCode shouldBe errorConst.errorCode
                sut.body?.message shouldBe errorConst.message
                sut.statusCode shouldBe errorConst.statusCode
            }
        }
    }

    given("NoResourceFoundException 발생하는 경우") {
        val exc = NoResourceFoundException(HttpMethod.GET, "/test")
        val handler = GlobalExceptionHandler()
        val errorConst = CommonErrorConst.AUTHENTICATION_ERROR

        `when`("예외 핸들러가 동작하면") {
            val sut = handler.handleNoResourceFoundException(e = exc)

            then("FailBody로 감싸서 반환된다") {
                sut.body?.errorCode shouldBe errorConst.errorCode
                sut.body?.message shouldBe errorConst.message
                sut.statusCode shouldBe errorConst.statusCode
            }
        }
    }
})
