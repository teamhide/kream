package com.teamhide.kream.bidding.adapter.out.persistence.jpa

import com.teamhide.kream.bidding.domain.model.Order

interface OrderQuerydslRepository {
    fun findByBiddingId(biddingId: Long): Order?
}
