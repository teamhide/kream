package com.teamhide.kream.bidding.application.service

import com.teamhide.kream.bidding.adapter.out.persistence.BiddingRepositoryAdapter
import com.teamhide.kream.bidding.application.exception.AlreadyCompleteBidException
import com.teamhide.kream.bidding.application.exception.BiddingNotFoundException
import com.teamhide.kream.bidding.domain.event.BiddingCompletedEvent
import com.teamhide.kream.bidding.domain.usecase.AttemptPaymentCommand
import com.teamhide.kream.bidding.domain.usecase.AttemptPaymentUseCase
import com.teamhide.kream.bidding.domain.usecase.CompleteBidCommand
import com.teamhide.kream.bidding.domain.usecase.CompleteBidUseCase
import com.teamhide.kream.bidding.domain.usecase.ImmediatePurchaseCommand
import com.teamhide.kream.bidding.domain.usecase.ImmediatePurchaseResponseDto
import com.teamhide.kream.bidding.domain.usecase.ImmediatePurchaseUseCase
import com.teamhide.kream.user.adapter.out.persistence.UserRepositoryAdapter
import com.teamhide.kream.user.application.exception.UserNotFoundException
import org.springframework.context.ApplicationEventPublisher
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
@Transactional(readOnly = true)
class ImmediatePurchaseService(
    private val biddingRepositoryAdapter: BiddingRepositoryAdapter,
    private val attemptPaymentUseCase: AttemptPaymentUseCase,
    private val completeBidUseCase: CompleteBidUseCase,
    private val userRepositoryAdapter: UserRepositoryAdapter,
    private val applicationEventPublisher: ApplicationEventPublisher,
) : ImmediatePurchaseUseCase {
    override fun execute(command: ImmediatePurchaseCommand): ImmediatePurchaseResponseDto {
        val bidding =
            biddingRepositoryAdapter.findById(biddingId = command.biddingId) ?: throw BiddingNotFoundException()
        if (!bidding.canBid()) {
            throw AlreadyCompleteBidException()
        }

        val user =
            userRepositoryAdapter.findById(userId = command.userId) ?: throw UserNotFoundException()

        // TODO: Lock

        val purchaserId = user.id
        val paymentId = AttemptPaymentCommand(biddingId = bidding.id, price = bidding.price, userId = purchaserId).let {
            attemptPaymentUseCase.execute(command = it)
        }

        CompleteBidCommand(paymentId = paymentId, biddingId = bidding.id, userId = purchaserId).let {
            completeBidUseCase.execute(command = it)
        }

        val event = BiddingCompletedEvent(productId = command.biddingId, biddingId = bidding.id)
        applicationEventPublisher.publishEvent(event)

        return ImmediatePurchaseResponseDto(biddingId = bidding.id, price = bidding.price)
    }
}
