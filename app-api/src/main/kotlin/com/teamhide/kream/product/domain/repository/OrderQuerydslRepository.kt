package com.teamhide.kream.product.domain.repository

import com.teamhide.kream.product.domain.model.Order

interface OrderQuerydslRepository {
    fun findByBiddingId(biddingId: Long): Order?
}
