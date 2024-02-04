package com.teamhide.kream.bidding.application.service

import com.teamhide.kream.bidding.adapter.out.persistence.BiddingRepositoryAdapter
import com.teamhide.kream.bidding.application.exception.AlreadyCompleteBidException
import com.teamhide.kream.bidding.application.exception.BiddingNotFoundException
import com.teamhide.kream.bidding.domain.event.BiddingCompletedEvent
import com.teamhide.kream.bidding.domain.usecase.AttemptPaymentCommand
import com.teamhide.kream.bidding.domain.usecase.AttemptPaymentUseCase
import com.teamhide.kream.bidding.domain.usecase.CompleteBidCommand
import com.teamhide.kream.bidding.domain.usecase.CompleteBidUseCase
import com.teamhide.kream.bidding.domain.usecase.ImmediateSaleCommand
import com.teamhide.kream.bidding.domain.usecase.ImmediateSaleResponseDto
import com.teamhide.kream.bidding.domain.usecase.ImmediateSaleUseCase
import com.teamhide.kream.user.adapter.out.persistence.UserRepositoryAdapter
import com.teamhide.kream.user.application.exception.UserNotFoundException
import org.springframework.context.ApplicationEventPublisher
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
@Transactional(readOnly = true)
class ImmediateSaleService(
    private val biddingRepositoryAdapter: BiddingRepositoryAdapter,
    private val userRepositoryAdapter: UserRepositoryAdapter,
    private val attemptPaymentUseCase: AttemptPaymentUseCase,
    private val completeBidUseCase: CompleteBidUseCase,
    private val applicationEventPublisher: ApplicationEventPublisher,
) : ImmediateSaleUseCase {
    override fun execute(command: ImmediateSaleCommand): ImmediateSaleResponseDto {
        val bidding =
            biddingRepositoryAdapter.findById(biddingId = command.biddingId) ?: throw BiddingNotFoundException()
        if (!bidding.canBid()) {
            throw AlreadyCompleteBidException()
        }

        userRepositoryAdapter.findById(userId = command.userId) ?: throw UserNotFoundException()

        // TODO: Lock

        val purchaserId = bidding.user.id
        val paymentId = AttemptPaymentCommand(biddingId = bidding.id, price = bidding.price, userId = purchaserId).let {
            attemptPaymentUseCase.execute(command = it)
        }

        CompleteBidCommand(paymentId = paymentId, biddingId = bidding.id, userId = purchaserId).let {
            completeBidUseCase.execute(command = it)
        }

        val event = BiddingCompletedEvent(productId = command.biddingId, biddingId = bidding.id)
        applicationEventPublisher.publishEvent(event)

        return ImmediateSaleResponseDto(biddingId = bidding.id, price = bidding.price)
    }
}
