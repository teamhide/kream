package com.teamhide.kream.bidding.adapter.out.persistence.jpa

import com.teamhide.kream.bidding.domain.model.SaleHistory

interface SaleHistoryQuerydslRepository {
    fun findByBiddingId(biddingId: Long): SaleHistory?
}
