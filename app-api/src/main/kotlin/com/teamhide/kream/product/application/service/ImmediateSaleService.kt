package com.teamhide.kream.product.application.service

import com.teamhide.kream.common.util.lock.RedisLock
import com.teamhide.kream.product.adapter.out.external.UserExternalAdapter
import com.teamhide.kream.product.adapter.out.persistence.BiddingRepositoryAdapter
import com.teamhide.kream.product.application.exception.AlreadyCompleteBidException
import com.teamhide.kream.product.application.exception.BiddingNotFoundException
import com.teamhide.kream.product.domain.event.BiddingCompletedEvent
import com.teamhide.kream.product.domain.usecase.AttemptPaymentCommand
import com.teamhide.kream.product.domain.usecase.AttemptPaymentUseCase
import com.teamhide.kream.product.domain.usecase.CompleteBidCommand
import com.teamhide.kream.product.domain.usecase.CompleteBidUseCase
import com.teamhide.kream.product.domain.usecase.ImmediateSaleCommand
import com.teamhide.kream.product.domain.usecase.ImmediateSaleResponseDto
import com.teamhide.kream.product.domain.usecase.ImmediateSaleUseCase
import com.teamhide.kream.user.application.exception.UserNotFoundException
import org.springframework.context.ApplicationEventPublisher
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
@Transactional(readOnly = true)
class ImmediateSaleService(
    private val biddingRepositoryAdapter: BiddingRepositoryAdapter,
    private val userExternalAdapter: UserExternalAdapter,
    private val attemptPaymentUseCase: AttemptPaymentUseCase,
    private val completeBidUseCase: CompleteBidUseCase,
    private val applicationEventPublisher: ApplicationEventPublisher,
) : ImmediateSaleUseCase {
    @RedisLock(key = "#command.biddingId")
    override fun execute(command: ImmediateSaleCommand): ImmediateSaleResponseDto {
        val bidding =
            biddingRepositoryAdapter.findById(biddingId = command.biddingId) ?: throw BiddingNotFoundException()
        if (!bidding.canBid()) {
            throw AlreadyCompleteBidException()
        }

        userExternalAdapter.findById(userId = command.userId) ?: throw UserNotFoundException()

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
