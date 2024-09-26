package com.teamhide.kream.product.ui.api.dto

import com.teamhide.kream.product.domain.usecase.BidResponseDto
import com.teamhide.kream.product.domain.vo.BiddingType

data class BidResponse(
    val biddingId: Long,
    val price: Int,
    val size: String,
    val biddingType: BiddingType,
) {
    companion object {
        fun from(bidResponseDto: BidResponseDto): BidResponse {
            return BidResponse(
                biddingId = bidResponseDto.biddingId,
                price = bidResponseDto.price,
                size = bidResponseDto.size,
                biddingType = bidResponseDto.biddingType,
            )
        }
    }
}
