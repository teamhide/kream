package com.teamhide.kream.bidding.adapter.out.persistence

import com.teamhide.kream.bidding.adapter.out.persistence.jpa.BiddingRepository
import com.teamhide.kream.bidding.adapter.out.persistence.jpa.SaleHistoryRepository
import com.teamhide.kream.bidding.domain.model.Bidding
import com.teamhide.kream.bidding.domain.vo.BiddingType
import org.springframework.stereotype.Component

@Component
class BiddingRepositoryAdapter(
    private val biddingRepository: BiddingRepository,
    private val saleHistoryRepository: SaleHistoryRepository,
) {
    fun findBiddingByPriceAndType(price: Int, biddingType: BiddingType): Bidding? {
        return biddingRepository.findByPriceAndBiddingType(price = price, biddingType = biddingType)
    }

    fun findMostExpensiveBid(price: Int, biddingType: BiddingType): Bidding? {
        return biddingRepository.findMostExpensiveBidding(price = price, biddingType = biddingType)
    }

    fun save(bidding: Bidding): Bidding {
        return biddingRepository.save(bidding)
    }
}
