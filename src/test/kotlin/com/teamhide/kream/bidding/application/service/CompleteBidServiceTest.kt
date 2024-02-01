package com.teamhide.kream.bidding.application.service

import com.teamhide.kream.bidding.adapter.out.persistence.BiddingRepositoryAdapter
import com.teamhide.kream.bidding.application.exception.BiddingNotFoundException
import com.teamhide.kream.bidding.domain.vo.BiddingStatus
import com.teamhide.kream.bidding.makeBidding
import com.teamhide.kream.bidding.makeCompleteBidCommand
import com.teamhide.kream.bidding.makeOrder
import com.teamhide.kream.bidding.makeSaleHistory
import com.teamhide.kream.user.adapter.out.persistence.UserRepositoryAdapter
import com.teamhide.kream.user.application.exception.UserNotFoundException
import com.teamhide.kream.user.makeUser
import io.kotest.assertions.throwables.shouldThrow
import io.kotest.core.spec.style.BehaviorSpec
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify

class CompleteBidServiceTest : BehaviorSpec({
    val biddingRepositoryAdapter = mockk<BiddingRepositoryAdapter>()
    val userRepositoryAdapter = mockk<UserRepositoryAdapter>()
    val completeBidService = CompleteBidService(
        biddingRepositoryAdapter = biddingRepositoryAdapter,
        userRepositoryAdapter = userRepositoryAdapter,
    )

    Given("존재하지 않는 유저가") {
        val command = makeCompleteBidCommand()
        every { userRepositoryAdapter.findById(any()) } returns null

        When("입찰 종료 요청을 하면") {
            Then("예외가 발생한다") {
                shouldThrow<UserNotFoundException> { completeBidService.execute(command = command) }
            }
        }
    }

    Given("존재하지 않는 입찰에 대해") {
        val command = makeCompleteBidCommand()
        val user = makeUser()
        every { userRepositoryAdapter.findById(any()) } returns user
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
        every { userRepositoryAdapter.findById(any()) } returns user

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
