package com.teamhide.kream.bidding.adapter.out.external

import com.teamhide.kream.client.WebClientException
import com.teamhide.kream.client.pg.AttemptPaymentRequest
import com.teamhide.kream.client.pg.PgClient
import org.springframework.retry.annotation.Backoff
import org.springframework.retry.annotation.Retryable
import org.springframework.stereotype.Component

@Component
class PgClientAdapter(
    private val pgClient: PgClient,
) {
    @Retryable(
        noRetryFor = [WebClientException::class],
        maxAttempts = 2,
        backoff = Backoff(delay = 300L)
    )
    fun attemptPayment(biddingId: Long, price: Int, userId: Long): Result<String> {
        val request = AttemptPaymentRequest(biddingId = biddingId, price = price, userId = userId)
        return runCatching {
            pgClient.attemptPayment(request = request).paymentId
        }
    }
}
