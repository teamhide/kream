package com.teamhide.kream.bidding.adapter.out.persistence

import com.teamhide.kream.bidding.adapter.out.persistence.jpa.BiddingRepository
import com.teamhide.kream.bidding.adapter.out.persistence.jpa.SaleHistoryRepository
import com.teamhide.kream.bidding.domain.vo.BiddingType
import com.teamhide.kream.bidding.makeBidding
import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.shouldBe
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify

class BiddingRepositoryAdapterTest : StringSpec({
    val biddingRepository = mockk<BiddingRepository>()
    val saleHistoryRepository = mockk<SaleHistoryRepository>()
    val biddingRepositoryAdapter = BiddingRepositoryAdapter(
        biddingRepository = biddingRepository, saleHistoryRepository = saleHistoryRepository,
    )

    "price와 biddingType으로 Bidding을 조회한다" {
        // Given
        val price = 1000
        val biddingType = BiddingType.SALE
        val bidding = makeBidding()
        every { biddingRepository.findByPriceAndBiddingType(any(), any()) } returns bidding

        // When
        val sut = biddingRepositoryAdapter.findBiddingByPriceAndType(price = price, biddingType = biddingType)!!

        // Then
        sut.id shouldBe bidding.id
        sut.biddingType shouldBe bidding.biddingType
        sut.user shouldBe bidding.user
        sut.product shouldBe bidding.product
        sut.price shouldBe bidding.price
        sut.size shouldBe bidding.size
        sut.status shouldBe bidding.status
        verify(exactly = 1) { biddingRepository.findByPriceAndBiddingType(any(), any()) }
    }

    "Bidding을 저장한다" {
        // Given
        val bidding = makeBidding()
        every { biddingRepository.save(any()) } returns bidding

        // When
        val sut = biddingRepositoryAdapter.save(bidding)

        // Then
        sut.id shouldBe bidding.id
        sut.biddingType shouldBe bidding.biddingType
        sut.user shouldBe bidding.user
        sut.product shouldBe bidding.product
        sut.price shouldBe bidding.price
        sut.size shouldBe bidding.size
        sut.status shouldBe bidding.status
        verify(exactly = 1) { biddingRepository.save(any()) }
    }

    "price와 biddingType으로 가장 금액이 큰 Bidding을 조회한다" {
        // Given
        val price = 1000
        val biddingType = BiddingType.SALE
        val bidding = makeBidding()
        every { biddingRepository.findMostExpensiveBidding(any(), any()) } returns bidding

        // When
        val sut = biddingRepositoryAdapter.findMostExpensiveBid(price = price, biddingType = biddingType)!!

        // Then
        sut.id shouldBe bidding.id
        sut.biddingType shouldBe bidding.biddingType
        sut.user shouldBe bidding.user
        sut.product shouldBe bidding.product
        sut.price shouldBe bidding.price
        sut.size shouldBe bidding.size
        sut.status shouldBe bidding.status
        verify(exactly = 1) { biddingRepository.findMostExpensiveBidding(any(), any()) }
    }
})
