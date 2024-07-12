package com.teamhide.kream.product.application.service

import com.teamhide.kream.product.application.exception.ImmediateTradeAvailableException
import com.teamhide.kream.product.application.exception.ProductNotFoundException
import com.teamhide.kream.product.domain.event.BiddingCreatedEvent
import com.teamhide.kream.product.domain.model.Bidding
import com.teamhide.kream.product.domain.repository.BiddingRepositoryAdapter
import com.teamhide.kream.product.domain.repository.ProductRepositoryAdapter
import com.teamhide.kream.product.domain.usecase.BidCommand
import com.teamhide.kream.product.domain.usecase.BidResponseDto
import com.teamhide.kream.product.domain.usecase.BidUseCase
import com.teamhide.kream.product.domain.vo.BiddingStatus
import com.teamhide.kream.product.domain.vo.BiddingType
import com.teamhide.kream.user.application.exception.UserNotFoundException
import org.springframework.context.ApplicationEventPublisher
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
@Transactional
class BidService(
    private val biddingRepositoryAdapter: BiddingRepositoryAdapter,
    private val productUserAdapter: ProductUserAdapter,
    private val productRepositoryAdapter: ProductRepositoryAdapter,
    private val applicationEventPublisher: ApplicationEventPublisher,
) : BidUseCase {
    override fun execute(command: BidCommand): BidResponseDto {
        if (!canBid(productId = command.productId, price = command.price, biddingType = command.biddingType)) {
            throw ImmediateTradeAvailableException()
        }

        val user = productUserAdapter.findById(userId = command.userId)
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
        val response = biddingRepositoryAdapter.save(bidding).let {
            BidResponseDto(
                biddingId = it.id,
                price = it.price,
                size = it.size,
                biddingType = it.biddingType,
            )
        }

        val event = BiddingCreatedEvent(
            productId = command.productId,
            biddingType = bidding.biddingType.name,
            biddingId = bidding.id,
            price = bidding.price,
        )
        applicationEventPublisher.publishEvent(event)
        return response
    }

    private fun canBid(productId: Long, price: Int, biddingType: BiddingType): Boolean {
        val bidding = getBiddingByType(productId = productId, biddingType = biddingType) ?: return true

        return when {
            price == bidding.price -> false
            biddingType == BiddingType.PURCHASE && price > bidding.price -> false
            biddingType == BiddingType.SALE && price < bidding.price -> false
            else -> true
        }
    }

    private fun getBiddingByType(productId: Long, biddingType: BiddingType): Bidding? {
        return if (biddingType == BiddingType.SALE) {
            biddingRepositoryAdapter.findMostExpensiveBidding(
                productId = productId, biddingType = BiddingType.PURCHASE
            )
        } else {
            biddingRepositoryAdapter.findMostCheapestBidding(
                productId = productId, biddingType = BiddingType.SALE
            )
        }
    }
}
