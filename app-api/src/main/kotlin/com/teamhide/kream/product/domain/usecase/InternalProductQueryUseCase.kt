package com.teamhide.kream.product.domain.usecase

import com.teamhide.kream.product.domain.model.Bidding
import com.teamhide.kream.product.domain.model.Product

data class GetBiddingByIdQuery(val biddingId: Long)

data class GetProductByIdQuery(val productId: Long)

interface InternalProductQueryUseCase {
    fun getBiddingById(query: GetBiddingByIdQuery): Bidding?

    fun getProductById(query: GetProductByIdQuery): Product?
}
