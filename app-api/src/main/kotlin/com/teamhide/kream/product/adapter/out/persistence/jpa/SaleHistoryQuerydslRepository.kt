package com.teamhide.kream.product.adapter.out.persistence.jpa

import com.teamhide.kream.product.domain.model.SaleHistory

interface SaleHistoryQuerydslRepository {
    fun findByBiddingId(biddingId: Long): SaleHistory?
}
