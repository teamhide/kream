package com.teamhide.kream.product.application.service

import com.teamhide.kream.product.application.exception.BiddingNotFoundException
import com.teamhide.kream.product.domain.repository.BiddingRepositoryAdapter
import com.teamhide.kream.product.domain.vo.BiddingStatus
import com.teamhide.kream.product.makeBidding
import com.teamhide.kream.product.makeCompleteBidCommand
import com.teamhide.kream.product.makeOrder
import com.teamhide.kream.product.makeSaleHistory
import com.teamhide.kream.user.application.exception.UserNotFoundException
import com.teamhide.kream.user.makeUser
import io.kotest.assertions.throwables.shouldThrow
import io.kotest.core.spec.style.BehaviorSpec
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify

internal class CompleteBidServiceTest : BehaviorSpec({
    val biddingRepositoryAdapter = mockk<BiddingRepositoryAdapter>()
    val userExternalAdapter = mockk<UserExternalAdapter>()
    val completeBidService = CompleteBidService(
        biddingRepositoryAdapter = biddingRepositoryAdapter,
        userExternalAdapter = userExternalAdapter,
    )

    Given("존재하지 않는 유저가") {
        val command = makeCompleteBidCommand()
        every { userExternalAdapter.findById(any()) } returns null

        When("입찰 종료 요청을 하면") {
            Then("예외가 발생한다") {
                shouldThrow<UserNotFoundException> { completeBidService.execute(command = command) }
            }
        }
    }

    Given("존재하지 않는 입찰에 대해") {
        val command = makeCompleteBidCommand()
        val user = makeUser()
        every { userExternalAdapter.findById(any()) } returns user
        every { biddingRepositoryAdapter.findById(any()) } returns null

        When("입찰 종료 요청을 하면") {
            Then("예외가 발생한다") {
                shouldThrow<BiddingNotFoundException> { completeBidService.execute(command = command) }
            }
        }
    }

    Given("입찰에 대해") {
        val bidding = makeBidding(status = BiddingStatus.IN_PROGRESS)
        every { biddingRepositoryAdapter.findById(any()) } returns bidding

        val command = makeCompleteBidCommand(biddingId = bidding.id)

        val user = makeUser()
        every { userExternalAdapter.findById(any()) } returns user

        val saleHistory = makeSaleHistory()
        every { biddingRepositoryAdapter.saveSaleHistory(any(), any()) } returns saleHistory

        val order = makeOrder()
        every { biddingRepositoryAdapter.saveOrder(any()) } returns order

        When("입찰 종료 요청을 하면") {
            completeBidService.execute(command = command)

            Then("거래 완료 처리된다") {
                verify(exactly = 1) { biddingRepositoryAdapter.saveOrder(any()) }
            }
        }
    }
})
