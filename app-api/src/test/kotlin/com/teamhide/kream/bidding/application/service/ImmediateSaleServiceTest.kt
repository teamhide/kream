package com.teamhide.kream.bidding.application.service

import com.teamhide.kream.bidding.adapter.out.persistence.BiddingRepositoryAdapter
import com.teamhide.kream.bidding.application.exception.AlreadyCompleteBidException
import com.teamhide.kream.bidding.application.exception.BiddingNotFoundException
import com.teamhide.kream.bidding.domain.event.BiddingCompletedEvent
import com.teamhide.kream.bidding.domain.usecase.AttemptPaymentUseCase
import com.teamhide.kream.bidding.domain.usecase.CompleteBidUseCase
import com.teamhide.kream.bidding.domain.vo.BiddingStatus
import com.teamhide.kream.bidding.domain.vo.BiddingType
import com.teamhide.kream.bidding.makeBidding
import com.teamhide.kream.bidding.makeImmediateSaleCommand
import com.teamhide.kream.user.adapter.out.persistence.UserRepositoryAdapter
import com.teamhide.kream.user.application.exception.UserNotFoundException
import com.teamhide.kream.user.makeUser
import io.kotest.assertions.throwables.shouldThrow
import io.kotest.core.spec.style.BehaviorSpec
import io.kotest.matchers.shouldBe
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.springframework.context.ApplicationEventPublisher

class ImmediateSaleServiceTest : BehaviorSpec({
    val biddingRepositoryAdapter = mockk<BiddingRepositoryAdapter>()
    val attemptPaymentUseCase = mockk<AttemptPaymentUseCase>()
    val completeBidUseCase = mockk<CompleteBidUseCase>()
    val userRepositoryAdapter = mockk<UserRepositoryAdapter>()
    val applicationEventPublisher = mockk<ApplicationEventPublisher>()
    val immediateSaleService = ImmediateSaleService(
        biddingRepositoryAdapter = biddingRepositoryAdapter,
        attemptPaymentUseCase = attemptPaymentUseCase,
        completeBidUseCase = completeBidUseCase,
        userRepositoryAdapter = userRepositoryAdapter,
        applicationEventPublisher = applicationEventPublisher,
    )

    Given("존재하지 않는 구매 입찰에 대해") {
        val command = makeImmediateSaleCommand()
        every { biddingRepositoryAdapter.findById(any()) } returns null

        When("즉시 판매 요청을 하면") {
            Then("예외가 발생한다") {
                shouldThrow<BiddingNotFoundException> { immediateSaleService.execute(command = command) }
            }
        }
    }

    Given("진행중이 아닌 구매 입찰에 대해") {
        val command = makeImmediateSaleCommand()

        val bidding = makeBidding(biddingType = BiddingType.PURCHASE, status = BiddingStatus.COMPLETE)
        every { biddingRepositoryAdapter.findById(any()) } returns bidding

        When("즉시 판매 요청을 하면") {
            Then("예외가 발생한다") {
                shouldThrow<AlreadyCompleteBidException> { immediateSaleService.execute(command = command) }
            }
        }
    }

    Given("존재하지 유저가") {
        val command = makeImmediateSaleCommand()
        val bidding = makeBidding(biddingType = BiddingType.PURCHASE, status = BiddingStatus.IN_PROGRESS)
        every { biddingRepositoryAdapter.findById(any()) } returns bidding
        every { userRepositoryAdapter.findById(any()) } returns null

        When("즉시 판매 요청을 하면") {
            Then("예외가 발생한다") {
                shouldThrow<UserNotFoundException> { immediateSaleService.execute(command = command) }
            }
        }
    }

    Given("구매 입찰에 대해") {
        val command = makeImmediateSaleCommand()
        val bidding = makeBidding(biddingType = BiddingType.PURCHASE, status = BiddingStatus.IN_PROGRESS)
        every { biddingRepositoryAdapter.findById(any()) } returns bidding

        val user = makeUser()
        every { userRepositoryAdapter.findById(any()) } returns user

        every { attemptPaymentUseCase.execute(any()) } returns "paymentId"

        every { completeBidUseCase.execute(any()) } returns Unit

        every { applicationEventPublisher.publishEvent(any<BiddingCompletedEvent>()) } returns Unit

        When("즉시 판매 요청을 하면") {
            val sut = immediateSaleService.execute(command = command)

            Then("예외가 발생한다") {
                sut.biddingId shouldBe bidding.id
                sut.price shouldBe bidding.price
                verify(exactly = 1) { applicationEventPublisher.publishEvent(any<BiddingCompletedEvent>()) }
            }
        }
    }
})