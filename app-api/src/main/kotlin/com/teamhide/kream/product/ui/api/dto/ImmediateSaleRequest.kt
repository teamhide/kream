package com.teamhide.kream.product.ui.api.dto

import com.teamhide.kream.product.domain.usecase.ImmediateSaleCommand
import jakarta.validation.constraints.NotNull

data class ImmediateSaleRequest(
    @field:NotNull
    val biddingId: Long,
) {
    fun toCommand(userId: Long): ImmediateSaleCommand {
        return ImmediateSaleCommand(
            biddingId = this.biddingId,
            userId = userId,
        )
    }
}
