package com.teamhide.kream.product.application.service

import com.teamhide.kream.product.application.exception.BiddingNotFoundException
import com.teamhide.kream.product.domain.model.Order
import com.teamhide.kream.product.domain.repository.BiddingRepositoryAdapter
import com.teamhide.kream.product.domain.usecase.CompleteBidCommand
import com.teamhide.kream.product.domain.usecase.CompleteBidUseCase
import com.teamhide.kream.product.domain.vo.BiddingStatus
import com.teamhide.kream.product.domain.vo.OrderStatus
import com.teamhide.kream.user.application.exception.UserNotFoundException
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Propagation
import org.springframework.transaction.annotation.Transactional

@Service
@Transactional(propagation = Propagation.REQUIRES_NEW)
class CompleteBidService(
    private val biddingRepositoryAdapter: BiddingRepositoryAdapter,
    private val productUserAdapter: ProductUserAdapter,
) : CompleteBidUseCase {
    override fun execute(command: CompleteBidCommand) {
        val user = productUserAdapter.findById(userId = command.userId) ?: throw UserNotFoundException()
        val bidding = biddingRepositoryAdapter.findById(biddingId = command.biddingId)
            ?: throw BiddingNotFoundException()

        bidding.changeStatus(status = BiddingStatus.COMPLETE)
        biddingRepositoryAdapter.saveSaleHistory(bidding = bidding, user = bidding.user)

        val order = Order(
            paymentId = command.paymentId,
            bidding = bidding,
            user = user,
            status = OrderStatus.COMPLETE,
        )
        biddingRepositoryAdapter.saveOrder(order = order)
    }
}
