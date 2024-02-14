package com.teamhide.kream.product.domain.usecase

import com.teamhide.kream.product.domain.model.Bidding

data class GetBiddingByIdQuery(val biddingId: Long)

interface GetBiddingByIdUseCase {
    fun execute(query: GetBiddingByIdQuery): Bidding?
}
