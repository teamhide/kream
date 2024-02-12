package com.teamhide.kream.product.adapter.out.persistence.jpa

import com.teamhide.kream.product.domain.model.Bidding
import com.teamhide.kream.product.domain.vo.BiddingType

interface BiddingQuerydslRepository {
    fun findMostExpensiveBidding(productId: Long, biddingType: BiddingType): Bidding?

    fun findMostCheapestBidding(productId: Long, biddingType: BiddingType): Bidding?
}
