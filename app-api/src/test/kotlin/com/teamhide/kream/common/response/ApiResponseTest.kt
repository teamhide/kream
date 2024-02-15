package com.teamhide.kream.common.response

import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.shouldBe
import org.springframework.http.HttpStatus

internal class ApiResponseTest : StringSpec({
    "HttpStatus만 존재하는 success" {
        val statusCode = HttpStatus.OK

        val sut = ApiResponse.success(statusCode)

        sut.body shouldBe null
        sut.statusCode shouldBe statusCode
    }

    "HttpStatus와 body모두 존재하는 success" {
        val statusCode = HttpStatus.NOT_FOUND
        val body = "body"

        val sut = ApiResponse.success(body, statusCode)

        sut.body shouldBe body
        sut.statusCode shouldBe statusCode
    }

    "HttpStatus만 존재하는 fail" {
        val statusCode = HttpStatus.BAD_REQUEST

        val sut = ApiResponse.fail(statusCode)

        sut.body shouldBe null
        sut.statusCode shouldBe statusCode
    }

    "HttpStatus와 body모두 존재하는 fail" {
        val statusCode = HttpStatus.BAD_REQUEST
        val body = FailBody(errorCode = "body", message = "")

        val sut = ApiResponse.fail(body = body, statusCode = statusCode)

        sut.body shouldBe body
        sut.body?.errorCode shouldBe body.errorCode
        sut.body?.message shouldBe body.message
        sut.statusCode shouldBe statusCode
    }
})
