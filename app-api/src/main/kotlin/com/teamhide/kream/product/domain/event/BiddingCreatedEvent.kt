package com.teamhide.kream.product.domain.event

data class BiddingCreatedEvent(
    val productId: Long,
    val biddingType: String,
    val price: Int,
    val biddingId: Long,
)
