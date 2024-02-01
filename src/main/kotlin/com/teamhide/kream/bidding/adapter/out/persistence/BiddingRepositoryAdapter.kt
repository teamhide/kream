package com.teamhide.kream.bidding.adapter.out.persistence

import com.teamhide.kream.bidding.adapter.out.persistence.jpa.BiddingRepository
import com.teamhide.kream.bidding.adapter.out.persistence.jpa.OrderRepository
import com.teamhide.kream.bidding.adapter.out.persistence.jpa.SaleHistoryRepository
import com.teamhide.kream.bidding.domain.model.Bidding
import com.teamhide.kream.bidding.domain.model.Order
import com.teamhide.kream.bidding.domain.model.SaleHistory
import com.teamhide.kream.bidding.domain.vo.BiddingType
import com.teamhide.kream.user.domain.model.User
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Component

@Component
class BiddingRepositoryAdapter(
    private val biddingRepository: BiddingRepository,
    private val saleHistoryRepository: SaleHistoryRepository,
    private val orderRepository: OrderRepository,
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

    fun findById(biddingId: Long): Bidding? {
        return biddingRepository.findByIdOrNull(biddingId)
    }

    fun saveSaleHistory(bidding: Bidding, user: User): SaleHistory {
        val saleHistory = SaleHistory(
            bidding = bidding,
            user = user,
            price = bidding.price,
            size = bidding.size,
        )
        return saleHistoryRepository.save(saleHistory)
    }

    fun saveOrder(order: Order): Order {
        return orderRepository.save(order)
    }
}
