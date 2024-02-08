package com.teamhide.kream.bidding.adapter.out.persistence.jpa

import com.teamhide.kream.bidding.domain.model.Bidding
import com.teamhide.kream.bidding.domain.vo.BiddingType

interface BiddingQuerydslRepository {
    fun findMostExpensiveBidding(productId: Long, biddingType: BiddingType): Bidding?

    fun findMostCheapestBidding(productId: Long, biddingType: BiddingType): Bidding?
}