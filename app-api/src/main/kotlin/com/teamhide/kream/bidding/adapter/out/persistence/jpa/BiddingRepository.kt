package com.teamhide.kream.bidding.adapter.out.persistence.jpa

import com.teamhide.kream.bidding.domain.model.Bidding
import com.teamhide.kream.bidding.domain.vo.BiddingType
import org.springframework.data.jpa.repository.JpaRepository

interface BiddingRepository : JpaRepository<Bidding, Long>, BiddingQuerydslRepository {
    fun findByPriceAndBiddingType(price: Int, biddingType: BiddingType): Bidding?
}
