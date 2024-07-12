package com.teamhide.kream.product.ui.api.dto

import com.teamhide.kream.product.domain.vo.BiddingType

data class BidResponse(
    val biddingId: Long,
    val price: Int,
    val size: String,
    val biddingType: BiddingType,
)
