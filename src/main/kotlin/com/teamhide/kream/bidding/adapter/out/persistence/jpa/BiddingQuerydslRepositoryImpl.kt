package com.teamhide.kream.bidding.adapter.out.persistence.jpa

import com.querydsl.jpa.impl.JPAQueryFactory
import com.teamhide.kream.bidding.domain.model.Bidding
import com.teamhide.kream.bidding.domain.model.QBidding
import com.teamhide.kream.bidding.domain.vo.BiddingType

class BiddingQuerydslRepositoryImpl(
    private val queryFactory: JPAQueryFactory,
) : BiddingQuerydslRepository {
    private val bidding = QBidding.bidding
    override fun findMostExpensiveBidding(price: Int, biddingType: BiddingType): Bidding? {
        return queryFactory
            .selectFrom(bidding)
            .where(bidding.biddingType.eq(biddingType))
            .orderBy(bidding.price.desc())
            .fetchFirst()
    }
}
