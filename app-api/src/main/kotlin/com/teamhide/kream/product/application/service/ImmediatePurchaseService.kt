package com.teamhide.kream.product.application.service

import com.teamhide.kream.common.util.lock.RedisLock
import com.teamhide.kream.product.application.exception.AlreadyCompleteBidException
import com.teamhide.kream.product.application.exception.BiddingNotFoundException
import com.teamhide.kream.product.domain.event.BiddingCompletedEvent
import com.teamhide.kream.product.domain.repository.BiddingRepositoryAdapter
import com.teamhide.kream.product.domain.usecase.AttemptPaymentCommand
import com.teamhide.kream.product.domain.usecase.AttemptPaymentUseCase
import com.teamhide.kream.product.domain.usecase.CompleteBidCommand
import com.teamhide.kream.product.domain.usecase.CompleteBidUseCase
import com.teamhide.kream.product.domain.usecase.ImmediatePurchaseCommand
import com.teamhide.kream.product.domain.usecase.ImmediatePurchaseResponseDto
import com.teamhide.kream.product.domain.usecase.ImmediatePurchaseUseCase
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
    private val productUserAdapter: ProductUserAdapter,
    private val applicationEventPublisher: ApplicationEventPublisher,
) : ImmediatePurchaseUseCase {
    @RedisLock(key = "#command.biddingId")
    override fun execute(command: ImmediatePurchaseCommand): ImmediatePurchaseResponseDto {
        val bidding =
            biddingRepositoryAdapter.findById(biddingId = command.biddingId) ?: throw BiddingNotFoundException()
        if (!bidding.canBid()) {
            throw AlreadyCompleteBidException()
        }

        val user =
            productUserAdapter.findById(userId = command.userId) ?: throw UserNotFoundException()

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
