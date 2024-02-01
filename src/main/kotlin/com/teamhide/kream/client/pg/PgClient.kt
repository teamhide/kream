package com.teamhide.kream.client.pg

import org.springframework.cloud.openfeign.FeignClient
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody

@FeignClient(
    name = "pg-client",
    url = "\${client.pg.url}",
    configuration = [PgClientConfig::class],
)
interface PgClient {
    @PostMapping("/pg/payment")
    fun attemptPayment(@RequestBody request: AttemptPaymentRequest): AttemptPaymentResponse

    @PostMapping("/pg/cancel")
    fun cancelPayment(@RequestBody request: CancelPaymentRequest)
}
