package com.teamhide.kream.bidding.application.service

import com.teamhide.kream.bidding.adapter.out.persistence.BiddingRepositoryAdapter
import com.teamhide.kream.bidding.application.exception.ImmediateTradeAvailableException
import com.teamhide.kream.bidding.domain.model.Bidding
import com.teamhide.kream.bidding.domain.usecase.BidCommand
import com.teamhide.kream.bidding.domain.usecase.BidResponseDto
import com.teamhide.kream.bidding.domain.usecase.BidUseCase
import com.teamhide.kream.bidding.domain.vo.BiddingStatus
import com.teamhide.kream.bidding.domain.vo.BiddingType
import com.teamhide.kream.product.adapter.out.persistence.ProductRepositoryAdapter
import com.teamhide.kream.product.application.exception.ProductNotFoundException
import com.teamhide.kream.user.adapter.out.persistence.UserRepositoryAdapter
import com.teamhide.kream.user.application.exception.UserNotFoundException
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
@Transactional
class BidService(
    private val biddingRepositoryAdapter: BiddingRepositoryAdapter,
    private val userRepositoryAdapter: UserRepositoryAdapter,
    private val productRepositoryAdapter: ProductRepositoryAdapter,
) : BidUseCase {
    override fun execute(command: BidCommand): BidResponseDto {
        if (!canBid(price = command.price, biddingType = command.biddingType)) {
            throw ImmediateTradeAvailableException()
        }

        val user = userRepositoryAdapter.findById(userId = command.userId)
            ?: throw UserNotFoundException()
        val product = productRepositoryAdapter.findById(productId = command.productId)
            ?: throw ProductNotFoundException()

        val bidding = Bidding(
            product = product,
            user = user,
            price = command.price,
            size = command.size,
            status = BiddingStatus.IN_PROGRESS,
            biddingType = command.biddingType,
        )
        return biddingRepositoryAdapter.save(bidding).let {
            BidResponseDto(
                biddingId = it.id,
                price = it.price,
                size = it.size,
                biddingType = it.biddingType,
            )
        }
    }

    private fun canBid(price: Int, biddingType: BiddingType): Boolean {
        val biddingTypeCondition = if (biddingType == BiddingType.SALE) {
            BiddingType.PURCHASE
        } else {
            BiddingType.SALE
        }

        val mostExpensiveBidding = (
            biddingRepositoryAdapter.findMostExpensiveBid(
                price = price, biddingType = biddingTypeCondition
            )
                ?: return true
            )

        if (price == mostExpensiveBidding.price) {
            return false
        }

        if (biddingType == BiddingType.PURCHASE && mostExpensiveBidding.price < price) {
            return false
        }

        if (biddingType == BiddingType.SALE && mostExpensiveBidding.price > price) {
            return false
        }

        return true
    }
}
