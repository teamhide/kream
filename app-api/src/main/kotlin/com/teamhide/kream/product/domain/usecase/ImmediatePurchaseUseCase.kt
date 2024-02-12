package com.teamhide.kream.product.domain.usecase

data class ImmediatePurchaseCommand(
    val biddingId: Long,
    val userId: Long,
)

data class ImmediatePurchaseResponseDto(
    val biddingId: Long,
    val price: Int,
)

interface ImmediatePurchaseUseCase {
    fun execute(command: ImmediatePurchaseCommand): ImmediatePurchaseResponseDto
}
