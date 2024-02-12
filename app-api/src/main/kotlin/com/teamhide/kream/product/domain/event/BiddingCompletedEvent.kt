package com.teamhide.kream.product.domain.event

data class BiddingCompletedEvent(
    val productId: Long,
    val biddingId: Long,
)
