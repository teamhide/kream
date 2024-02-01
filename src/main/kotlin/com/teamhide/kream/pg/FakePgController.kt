package com.teamhide.kream.pg

import com.teamhide.kream.client.pg.PgClient
import com.teamhide.kream.common.response.ApiResponse
import jakarta.validation.Valid
import jakarta.validation.constraints.NotBlank
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import java.util.UUID

data class PgAttemptPaymentResponse(val paymentId: String)

data class PgCancelPaymentRequest(
    @field:NotBlank
    val paymentId: String,
)

@RestController
@RequestMapping("/pg")
class FakePgController(
    private val pgClient: PgClient,
) {
    @PostMapping("/payment")
    fun attemptPayment(): ApiResponse<PgAttemptPaymentResponse> {
        val paymentId = UUID.randomUUID().toString()
        val response = PgAttemptPaymentResponse(paymentId = paymentId)
        return ApiResponse.success(body = response, statusCode = HttpStatus.OK)
    }

    @PostMapping("/cancel")
    fun cancelPayment(@RequestBody @Valid body: PgCancelPaymentRequest): ApiResponse<Void> {
        return ApiResponse.success(statusCode = HttpStatus.OK)
    }
}
