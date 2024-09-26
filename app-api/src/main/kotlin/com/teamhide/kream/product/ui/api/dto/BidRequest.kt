package com.teamhide.kream.product.ui.api.dto

import com.teamhide.kream.product.domain.usecase.BidCommand
import com.teamhide.kream.product.domain.vo.BiddingType
import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.NotNull

data class BidRequest(
    @field:NotNull
    val productId: Long,

    @field:NotNull
    val price: Int,

    @field:NotBlank
    val size: String,

    @field:NotNull
    val biddingType: BiddingType,
) {
    fun toCommand(userId: Long): BidCommand {
        return BidCommand(
            productId = this.productId,
            price = this.price,
            size = this.size,
            biddingType = this.biddingType,
            userId = userId,
        )
    }
}
