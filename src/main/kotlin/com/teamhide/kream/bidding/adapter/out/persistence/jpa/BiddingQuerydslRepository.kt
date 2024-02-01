package com.teamhide.kream.bidding.adapter.out.persistence.jpa

import com.teamhide.kream.bidding.domain.model.Bidding
import com.teamhide.kream.bidding.domain.vo.BiddingType

interface BiddingQuerydslRepository {
    fun findMostExpensiveBidding(biddingType: BiddingType): Bidding?
}
