package com.teamhide.kream.product.adapter.out.persistence.jpa

import com.teamhide.kream.product.domain.model.Bidding
import com.teamhide.kream.product.domain.vo.BiddingType
import org.springframework.data.jpa.repository.JpaRepository

interface BiddingRepository : JpaRepository<Bidding, Long>, BiddingQuerydslRepository {
    fun findByPriceAndBiddingType(price: Int, biddingType: BiddingType): Bidding?
}
