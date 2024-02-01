package com.teamhide.kream.bidding.adapter.out.persistence.jpa

import com.querydsl.jpa.impl.JPAQueryFactory
import com.teamhide.kream.bidding.domain.model.QSaleHistory
import com.teamhide.kream.bidding.domain.model.SaleHistory

class SaleHistoryQuerydslRepositoryImpl(
    private val queryFactory: JPAQueryFactory,
) : SaleHistoryQuerydslRepository {
    private val saleHistory = QSaleHistory.saleHistory

    override fun findByBiddingId(biddingId: Long): SaleHistory? {
        return queryFactory
            .selectFrom(saleHistory)
            .where(saleHistory.bidding.id.eq(biddingId))
            .fetchFirst()
    }
}
