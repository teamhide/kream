package com.teamhide.kream.product.application.service

import com.teamhide.kream.product.application.exception.BiddingNotFoundException
import com.teamhide.kream.product.domain.repository.BiddingRepository
import com.teamhide.kream.product.domain.usecase.GetBiddingByIdQuery
import com.teamhide.kream.product.makeBidding
import com.teamhide.kream.support.test.IntegrationTest
import com.teamhide.kream.support.test.MysqlDbCleaner
import io.kotest.assertions.throwables.shouldThrow
import io.kotest.core.spec.style.BehaviorSpec
import io.kotest.matchers.nulls.shouldNotBeNull
import io.kotest.matchers.shouldBe

@IntegrationTest
internal class GetBiddingByIdServiceTest(
    private val biddingRepository: BiddingRepository,
    private val getBiddingByIdService: GetBiddingByIdService,
) : BehaviorSpec({
    listeners(MysqlDbCleaner())

    Given("존재하지 않는 Bidding을") {
        val query = GetBiddingByIdQuery(biddingId = 1L)

        When("조회 요청하면") {
            Then("예외가 발생한다") {
                shouldThrow<BiddingNotFoundException> { getBiddingByIdService.execute(query = query) }
            }
        }
    }

    Given("존재하는 Bidding을") {
        val bidding = makeBidding(id = 1L)
        val query = GetBiddingByIdQuery(biddingId = bidding.id)
        biddingRepository.save(bidding)

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
