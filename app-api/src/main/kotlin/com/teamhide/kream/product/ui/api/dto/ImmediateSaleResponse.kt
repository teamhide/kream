package com.teamhide.kream.product.ui.api.dto

import com.teamhide.kream.product.domain.usecase.ImmediateSaleResponseDto

data class ImmediateSaleResponse(
    val biddingId: Long,
    val price: Int,
) {
    companion object {
        fun from(responseDto: ImmediateSaleResponseDto): ImmediateSaleResponse {
            return ImmediateSaleResponse(
                biddingId = responseDto.biddingId,
                price = responseDto.price,
            )
        }
    }
}
