package com.teamhide.kream.bidding.application.service

import com.teamhide.kream.bidding.adapter.out.persistence.BiddingRepositoryAdapter
import com.teamhide.kream.bidding.application.exception.BiddingNotFoundException
import com.teamhide.kream.bidding.domain.model.Order
import com.teamhide.kream.bidding.domain.usecase.CompleteBidCommand
import com.teamhide.kream.bidding.domain.usecase.CompleteBidUseCase
import com.teamhide.kream.bidding.domain.vo.BiddingStatus
import com.teamhide.kream.bidding.domain.vo.OrderStatus
import com.teamhide.kream.user.adapter.out.persistence.UserRepositoryAdapter
import com.teamhide.kream.user.application.exception.UserNotFoundException
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Propagation
import org.springframework.transaction.annotation.Transactional

@Service
@Transactional(propagation = Propagation.REQUIRES_NEW)
class CompleteBidService(
    private val biddingRepositoryAdapter: BiddingRepositoryAdapter,
    private val userRepositoryAdapter: UserRepositoryAdapter,
) : CompleteBidUseCase {
    override fun execute(command: CompleteBidCommand) {
        val user = userRepositoryAdapter.findById(userId = command.userId) ?: throw UserNotFoundException()
        val bidding = biddingRepositoryAdapter.findById(biddingId = command.biddingId)
            ?: throw BiddingNotFoundException()

        bidding.changeStatus(status = BiddingStatus.COMPLETE)
        biddingRepositoryAdapter.saveSaleHistory(bidding = bidding, user = user)

        val order = Order(
            paymentId = command.paymentId,
            bidding = bidding,
            user = user,
            status = OrderStatus.COMPLETE,
        )
        biddingRepositoryAdapter.saveOrder(order = order)
    }
}
