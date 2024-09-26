package com.teamhide.kream.product.ui.api.dto

import com.teamhide.kream.product.domain.usecase.ImmediatePurchaseCommand
import jakarta.validation.constraints.NotNull

data class ImmediatePurchaseRequest(
    @field:NotNull
    val biddingId: Long,
) {
    fun toCommand(userId: Long): ImmediatePurchaseCommand {
        return ImmediatePurchaseCommand(
            biddingId = this.biddingId,
            userId = userId,
        )
    }
}
