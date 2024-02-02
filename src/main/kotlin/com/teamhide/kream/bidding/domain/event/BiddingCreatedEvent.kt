package com.teamhide.kream.bidding.domain.event

data class BiddingCreatedEvent(
    val productId: Long,
    val biddingType: String,
    val price: Int,
)
