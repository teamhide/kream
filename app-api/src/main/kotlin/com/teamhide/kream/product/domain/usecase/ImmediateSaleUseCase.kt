package com.teamhide.kream.product.domain.usecase

data class ImmediateSaleCommand(
    val biddingId: Long,
    val userId: Long,
)

data class ImmediateSaleResponseDto(
    val biddingId: Long,
    val price: Int,
)

interface ImmediateSaleUseCase {
    fun execute(command: ImmediateSaleCommand): ImmediateSaleResponseDto
}
