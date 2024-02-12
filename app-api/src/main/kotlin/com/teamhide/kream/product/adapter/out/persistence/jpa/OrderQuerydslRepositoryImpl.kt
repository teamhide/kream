package com.teamhide.kream.product.adapter.out.persistence.jpa

import com.querydsl.jpa.impl.JPAQueryFactory
import com.teamhide.kream.product.domain.model.Order
import com.teamhide.kream.product.domain.model.QOrder

class OrderQuerydslRepositoryImpl(
    private val queryFactory: JPAQueryFactory,
) : OrderQuerydslRepository {
    private val order = QOrder.order

    override fun findByBiddingId(biddingId: Long): Order? {
        return queryFactory
            .selectFrom(order)
            .where(order.bidding.id.eq(biddingId))
            .fetchFirst()
    }
}
