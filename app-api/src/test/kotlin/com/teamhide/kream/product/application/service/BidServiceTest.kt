package com.teamhide.kream.product.application.service

import com.teamhide.kream.product.adapter.out.external.UserExternalAdapter
import com.teamhide.kream.product.adapter.out.persistence.BiddingRepositoryAdapter
import com.teamhide.kream.product.adapter.out.persistence.ProductRepositoryAdapter
import com.teamhide.kream.product.application.exception.ImmediateTradeAvailableException
import com.teamhide.kream.product.application.exception.ProductNotFoundException
import com.teamhide.kream.product.domain.event.BiddingCreatedEvent
import com.teamhide.kream.product.domain.model.InvalidBiddingPriceException
import com.teamhide.kream.product.domain.vo.BiddingType
import com.teamhide.kream.product.makeBidCommand
import com.teamhide.kream.product.makeBidding
import com.teamhide.kream.product.makeProduct
import com.teamhide.kream.user.application.exception.UserNotFoundException
import com.teamhide.kream.user.makeUser
import io.kotest.assertions.throwables.shouldThrow
import io.kotest.core.spec.IsolationMode
import io.kotest.core.spec.style.BehaviorSpec
import io.kotest.matchers.shouldBe
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.springframework.context.ApplicationEventPublisher

internal class BidServiceTest : BehaviorSpec({
    isolationMode = IsolationMode.InstancePerLeaf

    val biddingRepositoryAdapter = mockk<BiddingRepositoryAdapter>()
    val userExternalAdapter = mockk<UserExternalAdapter>()
    val productRepositoryAdapter = mockk<ProductRepositoryAdapter>()
    val applicationEventPublisher = mockk<ApplicationEventPublisher>()
    val bidService = BidService(
        biddingRepositoryAdapter = biddingRepositoryAdapter,
        userExternalAdapter = userExternalAdapter,
        productRepositoryAdapter = productRepositoryAdapter,
        applicationEventPublisher = applicationEventPublisher,
    )

    Given("동일한 가격의 구매 입찰이 있을 때") {
        val price = 1000
        val purchaseBidding = makeBidding(price = price, biddingType = BiddingType.SALE)
        every {
            biddingRepositoryAdapter.findMostExpensiveBidding(any(), any())
        } returns purchaseBidding

        val command = makeBidCommand(price = price, biddingType = BiddingType.SALE)

        When("판매 입찰을 시도하면") {
            Then("예외가 발생한다") {
                shouldThrow<ImmediateTradeAvailableException> { bidService.execute(command = command) }
            }
        }
    }

    Given("동일한 가격의 판매 입찰이 있을 때") {
        val price = 1000
        val purchaseBidding = makeBidding(price = price, biddingType = BiddingType.SALE)
        every {
            biddingRepositoryAdapter.findMostCheapestBidding(any(), any())
        } returns purchaseBidding

        val command = makeBidCommand(price = price, biddingType = BiddingType.PURCHASE)

        When("구매 입찰을 시도하면") {
            Then("예외가 발생한다") {
                shouldThrow<ImmediateTradeAvailableException> { bidService.execute(command = command) }
            }
        }
    }

    Given("판매 입찰 가격이 가장 낮은 구매 입찰 가격보다 낮은 경우") {
        val bidPrice = 2000
        val command = makeBidCommand(price = bidPrice, biddingType = BiddingType.SALE)

        val purchaseBidding = makeBidding(price = 3000, biddingType = BiddingType.PURCHASE)
        every {
            biddingRepositoryAdapter.findMostExpensiveBidding(any(), any())
        } returns purchaseBidding

        When("판매 입찰을 시도하면") {
            Then("예외가 발생한다") {
                shouldThrow<ImmediateTradeAvailableException> { bidService.execute(command = command) }
            }
        }
    }

    Given("구매 입찰 가격이 가장 높은 판매 입찰 가격보다 높은 경우") {
        val bidPrice = 3000
        val command = makeBidCommand(price = bidPrice, biddingType = BiddingType.PURCHASE)

        val purchaseBidding = makeBidding(price = 2000, biddingType = BiddingType.SALE)
        every {
            biddingRepositoryAdapter.findMostCheapestBidding(any(), any())
        } returns purchaseBidding

        When("판매 입찰을 시도하면") {
            Then("예외가 발생한다") {
                shouldThrow<ImmediateTradeAvailableException> { bidService.execute(command = command) }
            }
        }
    }

    Given("존재하지 않는 유저가") {
        val price = 1000
        val purchaseBidding = makeBidding(price = 2000, biddingType = BiddingType.SALE)
        every {
            biddingRepositoryAdapter.findMostCheapestBidding(any(), any())
        } returns purchaseBidding
        val command = makeBidCommand(price = price, biddingType = BiddingType.PURCHASE)

        every { userExternalAdapter.findById(any()) } returns null

        When("구매 입찰을 시도하면") {
            Then("예외가 발생한다") {
                shouldThrow<UserNotFoundException> { bidService.execute(command = command) }
            }
        }
    }

    Given("존재하지 않는 상품을 대상으로") {
        val price = 1000
        every {
            biddingRepositoryAdapter.findMostCheapestBidding(any(), any())
        } returns null
        val command = makeBidCommand(price = price, biddingType = BiddingType.PURCHASE)

        val user = makeUser()
        every { userExternalAdapter.findById(any()) } returns user

        every { productRepositoryAdapter.findById(any()) } returns null

        When("구매 입찰을 시도하면") {
            Then("예외가 발생한다") {
                shouldThrow<ProductNotFoundException> { bidService.execute(command = command) }
            }
        }
    }

    Given("0원 미만의 가격으로") {
        val price = -1
        every {
            biddingRepositoryAdapter.findMostCheapestBidding(any(), any())
        } returns null
        val command = makeBidCommand(price = price, biddingType = BiddingType.PURCHASE)

        val user = makeUser()
        every { userExternalAdapter.findById(any()) } returns user

        val product = makeProduct()
        every { productRepositoryAdapter.findById(any()) } returns product

        When("구매 입찰을 시도하면") {
            Then("예외가 발생한다") {
                shouldThrow<InvalidBiddingPriceException> { bidService.execute(command = command) }
            }
        }
    }

    Given("동일한 판매 가격이 없는 상품을 대상으로") {
        val price = 1000
        every {
            biddingRepositoryAdapter.findMostCheapestBidding(any(), any())
        } returns null

        val command = makeBidCommand(price = price, biddingType = BiddingType.PURCHASE)

        val user = makeUser()
        every { userExternalAdapter.findById(any()) } returns user

        val product = makeProduct()
        every { productRepositoryAdapter.findById(any()) } returns product

        val bidding = makeBidding()
        every { biddingRepositoryAdapter.save(any()) } returns bidding

        every { applicationEventPublisher.publishEvent(any<BiddingCreatedEvent>()) } returns Unit

        When("구매 입찰을 시도하면") {
            Then("입찰에 성공한다") {
                val sut = bidService.execute(command = command)
                sut.biddingId shouldBe bidding.id
                sut.biddingType shouldBe bidding.biddingType
                sut.price shouldBe bidding.price
                sut.size shouldBe bidding.size
                verify(exactly = 1) { applicationEventPublisher.publishEvent(any<BiddingCreatedEvent>()) }
            }
        }
    }

    Given("동일한 구매 가격이 없는 상품을 대상으로") {
        val price = 1000
        every {
            biddingRepositoryAdapter.findMostExpensiveBidding(any(), any())
        } returns null

        val command = makeBidCommand(price = price, biddingType = BiddingType.SALE)

        val user = makeUser()
        every { userExternalAdapter.findById(any()) } returns user

        val product = makeProduct()
        every { productRepositoryAdapter.findById(any()) } returns product

        val bidding = makeBidding()
        every { biddingRepositoryAdapter.save(any()) } returns bidding

        every { applicationEventPublisher.publishEvent(any<BiddingCreatedEvent>()) } returns Unit

        When("판매 입찰을 시도하면") {
            Then("입찰에 성공한다") {
                val sut = bidService.execute(command = command)
                sut.biddingId shouldBe bidding.id
                sut.biddingType shouldBe bidding.biddingType
                sut.price shouldBe bidding.price
                sut.size shouldBe bidding.size
                verify(exactly = 1) { applicationEventPublisher.publishEvent(any<BiddingCreatedEvent>()) }
            }
        }
    }
})
