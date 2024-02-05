package com.teamhide.kream.bidding.adapter.out.persistence

import com.teamhide.kream.bidding.adapter.out.persistence.jpa.BiddingRepository
import com.teamhide.kream.bidding.adapter.out.persistence.jpa.OrderRepository
import com.teamhide.kream.bidding.adapter.out.persistence.jpa.SaleHistoryRepository
import com.teamhide.kream.bidding.domain.vo.BiddingType
import com.teamhide.kream.bidding.makeBidding
import com.teamhide.kream.bidding.makeOrder
import com.teamhide.kream.bidding.makeSaleHistory
import com.teamhide.kream.user.makeUser
import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.nulls.shouldNotBeNull
import io.kotest.matchers.shouldBe
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.springframework.data.repository.findByIdOrNull

class BiddingRepositoryAdapterTest : StringSpec({
    val biddingRepository = mockk<BiddingRepository>()
    val saleHistoryRepository = mockk<SaleHistoryRepository>()
    val orderRepository = mockk<OrderRepository>()
    val biddingRepositoryAdapter = BiddingRepositoryAdapter(
        orderRepository = orderRepository,
        biddingRepository = biddingRepository,
        saleHistoryRepository = saleHistoryRepository,
    )

    "price와 biddingType으로 Bidding을 조회한다" {
        // Given
        val price = 1000
        val biddingType = BiddingType.SALE
        val bidding = makeBidding()
        every { biddingRepository.findByPriceAndBiddingType(any(), any()) } returns bidding

        // When
        val sut = biddingRepositoryAdapter.findBiddingByPriceAndType(price = price, biddingType = biddingType)

        // Then
        sut.shouldNotBeNull()
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
        val biddingType = BiddingType.SALE
        val bidding = makeBidding()
        every { biddingRepository.findMostExpensiveBidding(any()) } returns bidding

        // When
        val sut = biddingRepositoryAdapter.findMostExpensiveBid(biddingType = biddingType)

        // Then
        sut.shouldNotBeNull()
        sut.id shouldBe bidding.id
        sut.biddingType shouldBe bidding.biddingType
        sut.user shouldBe bidding.user
        sut.product shouldBe bidding.product
        sut.price shouldBe bidding.price
        sut.size shouldBe bidding.size
        sut.status shouldBe bidding.status
        verify(exactly = 1) { biddingRepository.findMostExpensiveBidding(any()) }
    }

    "id로 Bidding을 조회한다" {
        // Given
        val biddingId = 1L
        val bidding = makeBidding()
        every { biddingRepository.findByIdOrNull(any()) } returns bidding

        // When
        val sut = biddingRepositoryAdapter.findById(biddingId = biddingId)

        // Then
        sut.shouldNotBeNull()
        sut.id shouldBe bidding.id
        sut.biddingType shouldBe bidding.biddingType
        sut.user shouldBe bidding.user
        sut.product shouldBe bidding.product
        sut.price shouldBe bidding.price
        sut.size shouldBe bidding.size
        sut.status shouldBe bidding.status
    }

    "Order를 저장한다" {
        // Given
        val order = makeOrder()
        every { orderRepository.save(any()) } returns order

        // When
        val sut = biddingRepositoryAdapter.saveOrder(order = order)

        // Then
        sut.id shouldBe order.id
        sut.bidding shouldBe order.bidding
        sut.user shouldBe order.user
        sut.paymentId shouldBe order.paymentId
        sut.status shouldBe order.status
    }

    "SaleHistory를 저장한다" {
        // Given
        val bidding = makeBidding()
        val user = makeUser()
        val saleHistory = makeSaleHistory()
        every { saleHistoryRepository.save(any()) } returns saleHistory

        // When
        val sut = biddingRepositoryAdapter.saveSaleHistory(bidding = bidding, user = user)

        // Then
        sut.id shouldBe saleHistory.id
        sut.bidding shouldBe saleHistory.bidding
        sut.user shouldBe saleHistory.user
        sut.price shouldBe saleHistory.price
        sut.size shouldBe saleHistory.size
    }

    "가장 비싼 입찰을 조회한다" {
        // Given
        val biddingType = BiddingType.SALE
        val bidding = makeBidding()
        every { biddingRepository.findMostExpensiveBidding(any(), any()) } returns bidding

        // When
        val sut = biddingRepositoryAdapter.findMostExpensiveBidding(productId = 1L, biddingType = biddingType)

        // Then
        sut.shouldNotBeNull()
        sut.id shouldBe bidding.id
        sut.biddingType shouldBe bidding.biddingType
        sut.user shouldBe bidding.user
        sut.product shouldBe bidding.product
        sut.price shouldBe bidding.price
        sut.size shouldBe bidding.size
        sut.status shouldBe bidding.status
        verify(exactly = 1) { biddingRepository.findMostExpensiveBidding(any(), any()) }
    }

    "가장 저렴한 입찰을 조회한다" {
        // Given
        val biddingType = BiddingType.SALE
        val bidding = makeBidding()
        every { biddingRepository.findMostCheapestBidding(any(), any()) } returns bidding

        // When
        val sut = biddingRepositoryAdapter.findMostCheapestBidding(productId = 1L, biddingType = biddingType)

        // Then
        sut.shouldNotBeNull()
        sut.id shouldBe bidding.id
        sut.biddingType shouldBe bidding.biddingType
        sut.user shouldBe bidding.user
        sut.product shouldBe bidding.product
        sut.price shouldBe bidding.price
        sut.size shouldBe bidding.size
        sut.status shouldBe bidding.status
        verify(exactly = 1) { biddingRepository.findMostCheapestBidding(any(), any()) }
    }
})
