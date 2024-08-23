package com.teamhide.kream.product.application.service

import com.teamhide.kream.product.application.exception.ImmediateTradeAvailableException
import com.teamhide.kream.product.application.exception.ProductNotFoundException
import com.teamhide.kream.product.domain.model.InvalidBiddingPriceException
import com.teamhide.kream.product.domain.repository.BiddingRepository
import com.teamhide.kream.product.domain.repository.ProductRepository
import com.teamhide.kream.product.domain.vo.BiddingType
import com.teamhide.kream.product.makeBidCommand
import com.teamhide.kream.product.makeBidding
import com.teamhide.kream.product.makeProduct
import com.teamhide.kream.support.test.IntegrationTest
import com.teamhide.kream.support.test.MysqlDbCleaner
import com.teamhide.kream.user.application.exception.UserNotFoundException
import com.teamhide.kream.user.domain.repository.UserRepository
import com.teamhide.kream.user.makeUser
import io.kotest.assertions.throwables.shouldThrow
import io.kotest.core.spec.style.BehaviorSpec
import io.kotest.matchers.shouldBe

@IntegrationTest
internal class BidServiceTest(
    private val biddingRepository: BiddingRepository,
    private val bidService: BidService,
    private val userRepository: UserRepository,
    private val productRepository: ProductRepository,
) : BehaviorSpec({
    listeners(MysqlDbCleaner())

    Given("BidService") {
        When("동일한 가격의 구매 입찰이 있을 때 판매 입찰을 시도하면") {
            val price = 1000
            val purchaseBidding = makeBidding(price = price, biddingType = BiddingType.PURCHASE)
            biddingRepository.save(purchaseBidding)

            val command = makeBidCommand(price = price, biddingType = BiddingType.SALE)

            Then("예외가 발생한다") {
                shouldThrow<ImmediateTradeAvailableException> { bidService.execute(command = command) }
            }
        }

        When("동일한 가격의 판매 입찰이 있을 때 구매 입찰을 시도하면") {
            val price = 1000
            val saleBidding = makeBidding(price = price, biddingType = BiddingType.SALE)
            biddingRepository.save(saleBidding)

            val command = makeBidCommand(price = price, biddingType = BiddingType.PURCHASE)

            Then("예외가 발생한다") {
                shouldThrow<ImmediateTradeAvailableException> { bidService.execute(command = command) }
            }
        }

        When("판매 입찰 가격이 가장 낮은 구매 입찰 가격보다 낮을 때 판매 입찰을 시도하면") {
            val price = 2000
            val saleBidding = makeBidding(price = price, biddingType = BiddingType.SALE)
            biddingRepository.save(saleBidding)

            val command = makeBidCommand(price = 3000, biddingType = BiddingType.PURCHASE)

            Then("예외가 발생한다") {
                shouldThrow<ImmediateTradeAvailableException> { bidService.execute(command = command) }
            }
        }

        When("구매 입찰 가격이 가장 낮은 판매 입찰 가격보다 낮을 때 구매 입찰을 시도하면") {
            val price = 3000
            val purchaseBidding = makeBidding(price = price, biddingType = BiddingType.PURCHASE)
            biddingRepository.save(purchaseBidding)

            val command = makeBidCommand(price = 2000, biddingType = BiddingType.SALE)

            Then("예외가 발생한다") {
                shouldThrow<ImmediateTradeAvailableException> { bidService.execute(command = command) }
            }
        }

        When("존재하지 않는 유저가 구매 입찰을 시도하면") {
            val saleBidding = makeBidding(price = 2000, biddingType = BiddingType.SALE)
            biddingRepository.save(saleBidding)

            val command = makeBidCommand(price = 1000, biddingType = BiddingType.PURCHASE)

            Then("예외가 발생한다") {
                shouldThrow<UserNotFoundException> { bidService.execute(command = command) }
            }
        }

        When("존재하지 않는 상품을 대상으로 구매 입찰을 시도하면") {
            val saleBidding = makeBidding(price = 2000, biddingType = BiddingType.SALE)
            biddingRepository.save(saleBidding)

            val user = makeUser(id = 1L)
            userRepository.save(user)

            val command = makeBidCommand(price = 1000, biddingType = BiddingType.PURCHASE, userId = user.id)

            Then("예외가 발생한다") {
                shouldThrow<ProductNotFoundException> { bidService.execute(command = command) }
            }
        }

        When("0원 미만의 가격으로 구매 입찰을 시도하면") {
            val user = makeUser(id = 1L)
            userRepository.save(user)

            val product = makeProduct(id = 1L)
            productRepository.save(product)

            val command = makeBidCommand(
                price = -1, biddingType = BiddingType.PURCHASE, userId = user.id, productId = 1L,
            )

            Then("예외가 발생한다") {
                shouldThrow<InvalidBiddingPriceException> { bidService.execute(command = command) }
            }
        }

        When("동일한 판매 가격이 없는 상품을 대상으로 구매 입찰을 시도하면") {
            val user = makeUser(id = 1L)
            userRepository.save(user)

            val product = makeProduct(id = 1L)
            productRepository.save(product)

            val command = makeBidCommand(
                price = 1000, biddingType = BiddingType.PURCHASE, userId = user.id, productId = 1L,
            )

            val sut = bidService.execute(command = command)

            Then("성공한다") {
                val bidding = biddingRepository.findAll()[0]
                sut.biddingId shouldBe bidding.id
                sut.biddingType shouldBe bidding.biddingType
                sut.price shouldBe bidding.price
                sut.size shouldBe bidding.size
            }
        }

        When("동일한 구매 가격이 없는 상품을 대상으로 판매 입찰을 시도하면") {
            val user = makeUser(id = 1L)
            userRepository.save(user)

            val product = makeProduct(id = 1L)
            productRepository.save(product)

            val command = makeBidCommand(
                price = 1000, biddingType = BiddingType.SALE, userId = user.id, productId = 1L,
            )

            val sut = bidService.execute(command = command)

            Then("성공한다") {
                val bidding = biddingRepository.findAll()[0]
                sut.biddingId shouldBe bidding.id
                sut.biddingType shouldBe bidding.biddingType
                sut.price shouldBe bidding.price
                sut.size shouldBe bidding.size
            }
        }
    }
})
