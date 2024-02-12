package com.teamhide.kream.product.adapter.out.persistence.jpa

import com.teamhide.kream.product.domain.model.Order

interface OrderQuerydslRepository {
    fun findByBiddingId(biddingId: Long): Order?
}
