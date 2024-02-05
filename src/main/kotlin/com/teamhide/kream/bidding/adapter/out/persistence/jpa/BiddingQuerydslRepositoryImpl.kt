package com.teamhide.kream.bidding.adapter.out.persistence.jpa

import com.querydsl.jpa.impl.JPAQueryFactory
import com.teamhide.kream.bidding.domain.model.Bidding
import com.teamhide.kream.bidding.domain.model.QBidding
import com.teamhide.kream.bidding.domain.vo.BiddingStatus
import com.teamhide.kream.bidding.domain.vo.BiddingType

class BiddingQuerydslRepositoryImpl(
    private val queryFactory: JPAQueryFactory,
) : BiddingQuerydslRepository {
    private val bidding = QBidding.bidding

    override fun findMostExpensiveBidding(productId: Long, biddingType: BiddingType): Bidding? {
        return queryFactory
            .selectFrom(bidding)
            .where(
                bidding.product.id.eq(productId),
                bidding.biddingType.eq(biddingType),
                bidding.status.eq(BiddingStatus.IN_PROGRESS),
            )
            .orderBy(bidding.price.desc())
            .fetchFirst()
    }

    override fun findMostCheapestBidding(productId: Long, biddingType: BiddingType): Bidding? {
        return queryFactory
            .selectFrom(bidding)
            .where(
                bidding.product.id.eq(productId),
                bidding.biddingType.eq(biddingType),
                bidding.status.eq(BiddingStatus.IN_PROGRESS),
            )
            .orderBy(bidding.price.asc())
            .fetchFirst()
    }
}
