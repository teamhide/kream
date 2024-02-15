package com.teamhide.kream.common.response

import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity

class ApiResponse<T> : ResponseEntity<T> {
    constructor(statusCode: HttpStatus) : super(statusCode)

    constructor(body: T, statusCode: HttpStatus) : super(body, statusCode)

    companion object {
        const val TEST = "a"
        fun success(statusCode: HttpStatus): ApiResponse<Void> {
            return ApiResponse(statusCode = statusCode)
        }

        fun <T> success(body: T, statusCode: HttpStatus): ApiResponse<T> {
            return ApiResponse(body = body, statusCode = statusCode)
        }

        fun fail(statusCode: HttpStatus): ApiResponse<Void> {
            return ApiResponse(statusCode = statusCode)
        }

        fun fail(body: FailBody, statusCode: HttpStatus): ApiResponse<FailBody> {
            return ApiResponse(body = body, statusCode = statusCode)
        }
    }
}
