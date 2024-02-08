package com.teamhide.kream.client

import com.teamhide.kream.bidding.domain.usecase.AttemptPaymentCommand
import com.teamhide.kream.client.pg.AttemptPaymentRequest
import com.teamhide.kream.client.pg.AttemptPaymentResponse
import com.teamhide.kream.client.pg.CancelPaymentRequest
import com.teamhide.kream.pg.PgAttemptPaymentResponse
import com.teamhide.kream.pg.PgCancelPaymentRequest

fun makeAttemptPaymentRequest(biddingId: Long = 1L, price: Int = 50000, userId: Long = 1L): AttemptPaymentRequest {
    return AttemptPaymentRequest(biddingId = biddingId, price = price, userId = userId)
}

fun makeAttemptPaymentResponse(paymentId: String = "paymentId"): AttemptPaymentResponse {
    return AttemptPaymentResponse(paymentId = paymentId)
}

fun makeCancelPaymentRequest(paymentId: String = "paymentId"): CancelPaymentRequest {
    return CancelPaymentRequest(paymentId = paymentId)
}

fun makePgCancelPaymentRequest(paymentId: String = "paymentId"): PgCancelPaymentRequest {
    return PgCancelPaymentRequest(paymentId = paymentId)
}

fun makePgAttemptPaymentResponse(paymentId: String = "paymentId"): PgAttemptPaymentResponse {
    return PgAttemptPaymentResponse(paymentId = paymentId)
}

fun makeAttemptPaymentCommand(biddingId: Long = 1L, price: Int = 20000, userId: Long = 1L): AttemptPaymentCommand {
    return AttemptPaymentCommand(
        biddingId = biddingId,
        price = price,
        userId = userId,
    )
}
