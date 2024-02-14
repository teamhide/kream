package com.teamhide.kream.delivery.domain.usecase

import com.teamhide.kream.product.domain.model.Bidding
import com.teamhide.kream.product.domain.model.Product

interface ProductExternalPort {
    fun findProductById(productId: Long): Product?

    fun findBiddingById(biddingId: Long): Bidding?
}
