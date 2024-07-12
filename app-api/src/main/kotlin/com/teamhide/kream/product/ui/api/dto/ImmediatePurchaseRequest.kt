package com.teamhide.kream.product.ui.api.dto

import jakarta.validation.constraints.NotNull

data class ImmediatePurchaseRequest(
    @field:NotNull
    val biddingId: Long,
)
