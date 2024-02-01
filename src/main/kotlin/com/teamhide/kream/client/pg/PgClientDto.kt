package com.teamhide.kream.client.pg

data class AttemptPaymentRequest(
    val biddingId: Long,
    val userId: Long,
    val price: Int,
)

data class AttemptPaymentResponse(val paymentId: String)

data class CancelPaymentRequest(val paymentId: String)
