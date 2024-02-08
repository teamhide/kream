package com.teamhide.kream.delivery.domain.usecase

data class InitializeDeliveryCommand(
    val productId: Long,
    val biddingId: Long,
)

interface InitializeDeliveryUseCase {
    fun execute(command: InitializeDeliveryCommand)
}
