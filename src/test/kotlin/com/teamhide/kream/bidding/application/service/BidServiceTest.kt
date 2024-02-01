package com.teamhide.kream.bidding.application.service

import com.teamhide.kream.bidding.adapter.out.persistence.BiddingRepositoryAdapter
import com.teamhide.kream.bidding.application.exception.ImmediateTradeAvailableException
import com.teamhide.kream.bidding.domain.model.InvalidBiddingPriceException
import com.teamhide.kream.bidding.domain.vo.BiddingType
import com.teamhide.kream.bidding.makeBidCommand
import com.teamhide.kream.bidding.makeBidding
import com.teamhide.kream.product.adapter.out.persistence.ProductRepositoryAdapter
import com.teamhide.kream.product.application.exception.ProductNotFoundException
import com.teamhide.kream.product.makeProduct
import com.teamhide.kream.user.adapter.out.persistence.UserRepositoryAdapter
import com.teamhide.kream.user.application.exception.UserNotFoundException
import com.teamhide.kream.user.makeUser
import io.kotest.assertions.throwables.shouldThrow
import io.kotest.core.spec.style.BehaviorSpec
import io.kotest.matchers.shouldBe
import io.mockk.every
import io.mockk.mockk

class BidServiceTest : BehaviorSpec({
    val biddingRepositoryAdapter = mockk<BiddingRepositoryAdapter>()
    val userRepositoryAdapter = mockk<UserRepositoryAdapter>()
    val productRepositoryAdapter = mockk<ProductRepositoryAdapter>()
    val bidService = BidService(
        biddingRepositoryAdapter = biddingRepositoryAdapter,
        userRepositoryAdapter = userRepositoryAdapter,
        productRepositoryAdapter = productRepositoryAdapter,
    )

    Given("동일한 가격의 구매 입찰이 있을 때") {
        val price = 1000
        val purchaseBidding = makeBidding(price = price, biddingType = BiddingType.PURCHASE)
        every {
            biddingRepositoryAdapter
                .findMostExpensiveBid(biddingType = BiddingType.PURCHASE)
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
            biddingRepositoryAdapter
                .findMostExpensiveBid(biddingType = BiddingType.SALE)
        } returns purchaseBidding

        val command = makeBidCommand(price = price, biddingType = BiddingType.PURCHASE)

        When("구매 입찰을 시도하면") {
            Then("예외가 발생한다") {
                shouldThrow<ImmediateTradeAvailableException> { bidService.execute(command = command) }
            }
        }
    }

    Given("판매 입찰 가격이 구매 입찰 가격보다 낮은 경우") {
        val bidPrice = 2000
        val command = makeBidCommand(price = bidPrice, biddingType = BiddingType.SALE)

        val purchaseBidding = makeBidding(price = 3000, biddingType = BiddingType.PURCHASE)
        every {
            biddingRepositoryAdapter
                .findMostExpensiveBid(biddingType = BiddingType.PURCHASE)
        } returns purchaseBidding

        When("판매 입찰을 시도하면") {
            Then("예외가 발생한다") {
                shouldThrow<ImmediateTradeAvailableException> { bidService.execute(command = command) }
            }
        }
    }

    Given("구매 입찰 가격이 판매 입찰 가격보다 높은 경우") {
        val bidPrice = 3000
        val command = makeBidCommand(price = bidPrice, biddingType = BiddingType.PURCHASE)

        val purchaseBidding = makeBidding(price = 2000, biddingType = BiddingType.SALE)
        every {
            biddingRepositoryAdapter
                .findMostExpensiveBid(biddingType = BiddingType.SALE)
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
            biddingRepositoryAdapter
                .findMostExpensiveBid(biddingType = BiddingType.SALE)
        } returns purchaseBidding
        val command = makeBidCommand(price = price, biddingType = BiddingType.PURCHASE)

        every { userRepositoryAdapter.findById(any()) } returns null

        When("구매 입찰을 시도하면") {
            Then("예외가 발생한다") {
                shouldThrow<UserNotFoundException> { bidService.execute(command = command) }
            }
        }
    }

    Given("존재하지 상품을 대상으로") {
        val price = 1000
        every {
            biddingRepositoryAdapter
                .findMostExpensiveBid(biddingType = BiddingType.SALE)
        } returns null
        val command = makeBidCommand(price = price, biddingType = BiddingType.PURCHASE)

        val user = makeUser()
        every { userRepositoryAdapter.findById(any()) } returns user

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
            biddingRepositoryAdapter
                .findMostExpensiveBid(biddingType = BiddingType.SALE)
        } returns null
        val command = makeBidCommand(price = price, biddingType = BiddingType.PURCHASE)

        val user = makeUser()
        every { userRepositoryAdapter.findById(any()) } returns user

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
            biddingRepositoryAdapter
                .findMostExpensiveBid(biddingType = BiddingType.SALE)
        } returns null

        val command = makeBidCommand(price = price, biddingType = BiddingType.PURCHASE)

        val user = makeUser()
        every { userRepositoryAdapter.findById(any()) } returns user

        val product = makeProduct()
        every { productRepositoryAdapter.findById(any()) } returns product

        val bidding = makeBidding()
        every { biddingRepositoryAdapter.save(any()) } returns bidding

        When("구매 입찰을 시도하면") {
            Then("입찰에 성공한다") {
                val sut = bidService.execute(command = command)
                sut.biddingId shouldBe bidding.id
                sut.biddingType shouldBe bidding.biddingType
                sut.price shouldBe bidding.price
                sut.size shouldBe bidding.size
            }
        }
    }
})
