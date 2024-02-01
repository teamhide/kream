package com.teamhide.kream.bidding.domain.usecase

data class AttemptPaymentCommand(val biddingId: Long, val price: Int)
interface AttemptPaymentUseCase {
    fun execute(command: AttemptPaymentCommand): String
}
