package com.teamhide.kream.product.domain.repository

import com.teamhide.kream.product.domain.model.SaleHistory

interface SaleHistoryQuerydslRepository {
    fun findByBiddingId(biddingId: Long): SaleHistory?
}
