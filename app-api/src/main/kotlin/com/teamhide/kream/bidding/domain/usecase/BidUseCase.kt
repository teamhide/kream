package com.teamhide.kream.bidding.domain.usecase

import com.teamhide.kream.bidding.domain.vo.BiddingType

data class BidCommand(
    val productId: Long,
    val price: Int,
    val size: String,
    val biddingType: BiddingType,
    val userId: Long,
)

data class BidResponseDto(
    val biddingId: Long,
    val price: Int,
    val size: String,
    val biddingType: BiddingType,
)

interface BidUseCase {
    fun execute(command: BidCommand): BidResponseDto
}
