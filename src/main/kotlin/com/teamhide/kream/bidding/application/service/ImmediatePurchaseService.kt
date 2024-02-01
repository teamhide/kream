package com.teamhide.kream.bidding.application.service

import com.teamhide.kream.bidding.adapter.out.persistence.BiddingRepositoryAdapter
import com.teamhide.kream.bidding.application.exception.BiddingNotFoundException
import com.teamhide.kream.bidding.domain.usecase.AttemptPaymentCommand
import com.teamhide.kream.bidding.domain.usecase.AttemptPaymentUseCase
import com.teamhide.kream.bidding.domain.usecase.CompleteBidCommand
import com.teamhide.kream.bidding.domain.usecase.CompleteBidUseCase
import com.teamhide.kream.bidding.domain.usecase.ImmediatePurchaseCommand
import com.teamhide.kream.bidding.domain.usecase.ImmediatePurchaseResponseDto
import com.teamhide.kream.bidding.domain.usecase.ImmediatePurchaseUseCase
import com.teamhide.kream.user.adapter.out.persistence.UserRepositoryAdapter
import com.teamhide.kream.user.application.exception.UserNotFoundException
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
@Transactional(readOnly = true)
class ImmediatePurchaseService(
    private val biddingRepositoryAdapter: BiddingRepositoryAdapter,
    private val attemptPaymentUseCase: AttemptPaymentUseCase,
    private val completeBidUseCase: CompleteBidUseCase,
    private val userRepositoryAdapter: UserRepositoryAdapter,
) : ImmediatePurchaseUseCase {
    override fun execute(command: ImmediatePurchaseCommand): ImmediatePurchaseResponseDto {
        val bidding =
            biddingRepositoryAdapter.findById(biddingId = command.biddingId) ?: throw BiddingNotFoundException()
        val user =
            userRepositoryAdapter.findById(userId = command.userId) ?: throw UserNotFoundException()

        // TODO: Lock

        val paymentId = AttemptPaymentCommand(biddingId = bidding.id, price = bidding.price).let {
            attemptPaymentUseCase.execute(command = it)
        }

        CompleteBidCommand(paymentId = paymentId, biddingId = bidding.id, userId = user.id).let {
            completeBidUseCase.execute(command = it)
        }

        return ImmediatePurchaseResponseDto(biddingId = bidding.id, price = bidding.price)
    }
}
