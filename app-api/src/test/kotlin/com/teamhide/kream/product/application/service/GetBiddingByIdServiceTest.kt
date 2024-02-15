package com.teamhide.kream.product.application.service

import com.teamhide.kream.product.adapter.out.persistence.BiddingRepositoryAdapter
import com.teamhide.kream.product.application.exception.BiddingNotFoundException
import com.teamhide.kream.product.domain.usecase.GetBiddingByIdQuery
import com.teamhide.kream.product.makeBidding
import io.kotest.assertions.throwables.shouldThrow
import io.kotest.core.spec.style.BehaviorSpec
import io.kotest.matchers.nulls.shouldNotBeNull
import io.kotest.matchers.shouldBe
import io.mockk.every
import io.mockk.mockk

internal class GetBiddingByIdServiceTest : BehaviorSpec({
    val biddingRepositoryAdapter = mockk<BiddingRepositoryAdapter>()
    val getBiddingByIdService = GetBiddingByIdService(biddingRepositoryAdapter = biddingRepositoryAdapter)

    Given("존재하지 않는 Bidding을") {
        val query = GetBiddingByIdQuery(biddingId = 1L)
        every { biddingRepositoryAdapter.findById(any()) } returns null

        When("조회 요청하면") {
            Then("예외가 발생한다") {
                shouldThrow<BiddingNotFoundException> { getBiddingByIdService.execute(query = query) }
            }
        }
    }

    Given("존재하는 Bidding을") {
        val bidding = makeBidding(id = 1L)
        val query = GetBiddingByIdQuery(biddingId = bidding.id)
        every { biddingRepositoryAdapter.findById(any()) } returns bidding

        When("조회 요청하면") {
            val sut = getBiddingByIdService.execute(query = query)

            Then("Bidding을 리턴한다") {
                sut.shouldNotBeNull()
                sut.id shouldBe bidding.id
                sut.biddingType shouldBe bidding.biddingType
                sut.price shouldBe bidding.price
                sut.status shouldBe bidding.status
                sut.size shouldBe bidding.size
                sut.product.id shouldBe bidding.product.id
                sut.user.id shouldBe bidding.user.id
            }
        }
    }
})
