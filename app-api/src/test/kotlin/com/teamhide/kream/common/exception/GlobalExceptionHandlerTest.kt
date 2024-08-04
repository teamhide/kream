package com.teamhide.kream.common.exception

import com.teamhide.kream.common.security.JwtAuthenticationFailException
import io.github.resilience4j.ratelimiter.RequestNotPermitted
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

internal class GlobalExceptionHandlerTest : BehaviorSpec({
    Given("CustomException이 발생하는 경우") {
        class TestException : CustomException(statusCode = HttpStatus.NOT_FOUND, errorCode = "NOT_FOUND", message = "message")
        val exc = TestException()
        val handler = GlobalExceptionHandler()

        When("예외 핸들러가 동작하면") {
            val sut = handler.handleCustomException(e = exc)

            Then("FailBody로 감싸서 반환된다") {
                sut.body?.errorCode shouldBe exc.errorCode
                sut.body?.message shouldBe exc.message
                sut.statusCode shouldBe exc.statusCode
            }
        }
    }

    Given("HttpMessageNotReadableException이 발생하는 경우") {
        val mockHttpInputMessage = mockk<HttpInputMessage>()
        val exc = HttpMessageNotReadableException("message", mockHttpInputMessage)
        val handler = GlobalExceptionHandler()
        val errorConst = CommonErrorConst.HTTP_MESSAGE_NOT_READABLE

        When("예외 핸들러가 동작하면") {
            val sut = handler.handleHttpMessageNotReadableException(e = exc)

            Then("FailBody로 감싸서 반환된다") {
                sut.body?.errorCode shouldBe errorConst.errorCode
                sut.body?.message shouldBe errorConst.message
                sut.statusCode shouldBe errorConst.statusCode
            }
        }
    }

    Given("HttpRequestMethodNotSupportedException 발생하는 경우") {
        val exc = HttpRequestMethodNotSupportedException("POST")
        val handler = GlobalExceptionHandler()
        val errorConst = CommonErrorConst.HTTP_REQUEST_METHOD_NOT_SUPPORTED

        When("예외 핸들러가 동작하면") {
            val sut = handler.handleHttpRequestMethodNotSupportedException(e = exc)

            Then("FailBody로 감싸서 반환된다") {
                sut.body?.errorCode shouldBe errorConst.errorCode
                sut.body?.message shouldBe errorConst.message
                sut.statusCode shouldBe errorConst.statusCode
            }
        }
    }

    Given("MethodArgumentNotValidException 발생하는 경우") {
        val mockMethodParameter = mockk<MethodParameter>()
        val mockBindingResult = mockk<BindingResult>()
        val exc = MethodArgumentNotValidException(mockMethodParameter, mockBindingResult)
        val handler = GlobalExceptionHandler()
        val errorConst = CommonErrorConst.METHOD_ARGUMENT_NOT_VALID

        When("예외 핸들러가 동작하면") {
            val sut = handler.handleMethodArgumentNotValidException(e = exc)

            Then("FailBody로 감싸서 반환된다") {
                sut.body?.errorCode shouldBe errorConst.errorCode
                sut.body?.message shouldBe errorConst.message
                sut.statusCode shouldBe errorConst.statusCode
            }
        }
    }

    Given("NoHandlerFoundException 발생하는 경우") {
        val mockHeader = mockk<HttpHeaders>()
        val exc = NoHandlerFoundException("POST", "URL", mockHeader)
        val handler = GlobalExceptionHandler()
        val errorConst = CommonErrorConst.NO_HANDLER_FOUND

        When("예외 핸들러가 동작하면") {
            val sut = handler.handleNoHandlerFoundException(e = exc)

            Then("FailBody로 감싸서 반환된다") {
                sut.body?.errorCode shouldBe errorConst.errorCode
                sut.body?.message shouldBe errorConst.message
                sut.statusCode shouldBe errorConst.statusCode
            }
        }
    }

    Given("MissingRequestHeaderException 발생하는 경우") {
        val mockMethodParameter = mockk<MethodParameter>()
        val exc = MissingRequestHeaderException("X-API-Key", mockMethodParameter)
        val handler = GlobalExceptionHandler()
        val errorConst = CommonErrorConst.MISSING_REQUEST_HEADER

        When("예외 핸들러가 동작하면") {
            val sut = handler.handleMissingRequestHeaderException(e = exc)

            Then("FailBody로 감싸서 반환된다") {
                sut.body?.errorCode shouldBe errorConst.errorCode
                sut.body?.message shouldBe errorConst.message
                sut.statusCode shouldBe errorConst.statusCode
            }
        }
    }

    Given("Exception 발생하는 경우") {
        val exc = Exception()
        val handler = GlobalExceptionHandler()
        val errorConst = CommonErrorConst.UNKNOWN

        When("예외 핸들러가 동작하면") {
            val sut = handler.handleException(e = exc)

            Then("FailBody로 감싸서 반환된다") {
                sut.body?.errorCode shouldBe errorConst.errorCode
                sut.body?.message shouldBe errorConst.message
                sut.statusCode shouldBe errorConst.statusCode
            }
        }
    }

    Given("AuthenticationException이 발생하는 경우") {
        val exc = JwtAuthenticationFailException()
        val handler = GlobalExceptionHandler()
        val errorConst = CommonErrorConst.AUTHENTICATION_ERROR

        When("예외 핸들러가 동작하면") {
            val sut = handler.handleAuthenticationException(e = exc)

            Then("FailBody로 감싸서 반환된다") {
                sut.body?.errorCode shouldBe errorConst.errorCode
                sut.body?.message shouldBe errorConst.message
                sut.statusCode shouldBe errorConst.statusCode
            }
        }
    }

    Given("RequestNotPermitted 발생하는 경우") {
        val exc = mockk<RequestNotPermitted>()
        val handler = GlobalExceptionHandler()
        val errorConst = CommonErrorConst.REQUEST_NOT_PERMITTED

        When("예외 핸들러가 동작하면") {
            val sut = handler.handleRequestNotPermittedException(e = exc)

            Then("FailBody로 감싸서 반환된다") {
                sut.body?.errorCode shouldBe errorConst.errorCode
                sut.body?.message shouldBe errorConst.message
                sut.statusCode shouldBe errorConst.statusCode
            }
        }
    }

    Given("NoResourceFoundException 발생하는 경우") {
        val exc = NoResourceFoundException(HttpMethod.GET, "/test")
        val handler = GlobalExceptionHandler()
        val errorConst = CommonErrorConst.AUTHENTICATION_ERROR

        When("예외 핸들러가 동작하면") {
            val sut = handler.handleNoResourceFoundException(e = exc)

            Then("FailBody로 감싸서 반환된다") {
                sut.body?.errorCode shouldBe errorConst.errorCode
                sut.body?.message shouldBe errorConst.message
                sut.statusCode shouldBe errorConst.statusCode
            }
        }
    }
})
