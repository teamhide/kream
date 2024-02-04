package com.teamhide.kream.bidding.application.service

import com.teamhide.kream.bidding.adapter.out.external.PgClientAdapter
import com.teamhide.kream.bidding.domain.usecase.AttemptPaymentCommand
import com.teamhide.kream.bidding.domain.usecase.AttemptPaymentUseCase
import io.github.oshai.kotlinlogging.KotlinLogging
import org.springframework.stereotype.Service

private val logger = KotlinLogging.logger { }

@Service
class AttemptPaymentService(
    private val pgClientAdapter: PgClientAdapter,
) : AttemptPaymentUseCase {
    override fun execute(command: AttemptPaymentCommand): String {
        return command.let {
            pgClientAdapter.attemptPayment(
                biddingId = it.biddingId,
                price = it.price,
                userId = it.userId,
            ).onFailure {
                logger.error { "AttemptPaymentService | Pg error. e=$it" }
            }.getOrThrow()
        }
    }
}
