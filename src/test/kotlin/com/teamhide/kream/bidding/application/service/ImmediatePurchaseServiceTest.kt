package com.teamhide.kream.bidding.application.service

import com.teamhide.kream.bidding.adapter.out.persistence.BiddingRepositoryAdapter
import com.teamhide.kream.bidding.application.exception.BiddingNotFoundException
import com.teamhide.kream.bidding.domain.usecase.AttemptPaymentUseCase
import com.teamhide.kream.bidding.domain.usecase.CompleteBidUseCase
import com.teamhide.kream.bidding.makeBidding
import com.teamhide.kream.bidding.makeImmediatePurchaseCommand
import com.teamhide.kream.user.adapter.out.persistence.UserRepositoryAdapter
import com.teamhide.kream.user.application.exception.UserNotFoundException
import com.teamhide.kream.user.makeUser
import io.kotest.assertions.throwables.shouldThrow
import io.kotest.core.spec.style.BehaviorSpec
import io.kotest.matchers.shouldBe
import io.mockk.every
import io.mockk.mockk

class ImmediatePurchaseServiceTest : BehaviorSpec({
    val biddingRepositoryAdapter = mockk<BiddingRepositoryAdapter>()
    val attemptPaymentUseCase = mockk<AttemptPaymentUseCase>()
    val completeBidUseCase = mockk<CompleteBidUseCase>()
    val userRepositoryAdapter = mockk<UserRepositoryAdapter>()
    val immediatePurchaseService = ImmediatePurchaseService(
        biddingRepositoryAdapter = biddingRepositoryAdapter,
        attemptPaymentUseCase = attemptPaymentUseCase,
        completeBidUseCase = completeBidUseCase,
        userRepositoryAdapter = userRepositoryAdapter,
    )

    Given("존재하지 않는 판매 입찰에 대해") {
        val command = makeImmediatePurchaseCommand()
        every { biddingRepositoryAdapter.findById(any()) } returns null

        When("즉시 구매 요청을 하면") {
            Then("예외가 발생한다") {
                shouldThrow<BiddingNotFoundException> { immediatePurchaseService.execute(command = command) }
            }
        }
    }

    Given("존재하지 않는 유저가") {
        val command = makeImmediatePurchaseCommand()
        val bidding = makeBidding()
        every { biddingRepositoryAdapter.findById(any()) } returns bidding
        every { userRepositoryAdapter.findById(any()) } returns null

        When("즉시 구매 요청을 하면") {
            Then("예외가 발생한다") {
                shouldThrow<UserNotFoundException> { immediatePurchaseService.execute(command = command) }
            }
        }
    }

    Given("입찰에 대해") {
        val command = makeImmediatePurchaseCommand()
        val bidding = makeBidding()
        every { biddingRepositoryAdapter.findById(any()) } returns bidding

        val user = makeUser()
        every { userRepositoryAdapter.findById(any()) } returns user

        every { attemptPaymentUseCase.execute(any()) } returns "paymentId"

        every { completeBidUseCase.execute(any()) } returns Unit

        When("즉시 구매 요청을 하면") {
            val sut = immediatePurchaseService.execute(command = command)

            Then("구매가 완료되며 입찰이 종료된다") {
                sut.biddingId shouldBe bidding.id
                sut.price shouldBe bidding.price
            }
        }
    }
})
