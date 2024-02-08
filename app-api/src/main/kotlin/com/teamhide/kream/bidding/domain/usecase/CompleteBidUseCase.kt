package com.teamhide.kream.bidding.domain.usecase

data class CompleteBidCommand(val paymentId: String, val biddingId: Long, val userId: Long)
interface CompleteBidUseCase {
    fun execute(command: CompleteBidCommand)
}
