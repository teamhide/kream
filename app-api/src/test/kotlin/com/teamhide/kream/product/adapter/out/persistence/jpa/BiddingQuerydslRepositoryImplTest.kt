package com.teamhide.kream.product.adapter.out.persistence.jpa

import com.teamhide.kream.product.domain.vo.BiddingType
import com.teamhide.kream.product.makeBidding
import com.teamhide.kream.product.makeProduct
import com.teamhide.kream.support.test.JpaRepositoryTest
import com.teamhide.kream.user.adapter.out.persistence.jpa.UserRepository
import com.teamhide.kream.user.makeUser
import io.kotest.matchers.nulls.shouldNotBeNull
import io.kotest.matchers.shouldBe
import org.junit.jupiter.api.Test

@JpaRepositoryTest
internal class BiddingQuerydslRepositoryImplTest(
    private val biddingRepository: BiddingRepository,
    private val userRepository: UserRepository,
    private val productRepository: ProductRepository,
) {
    @Test
    fun `특정 상품에 대해 가장 비싼 입찰을 조회한다`() {
        // Given
        val user = userRepository.save(makeUser(id = 100L))
        val product = productRepository.save(makeProduct(id = 1L))
        biddingRepository.save(
            makeBidding(
                biddingType = BiddingType.SALE,
                price = 1000,
                product = product,
                user = user
            )
        )
        biddingRepository.save(
            makeBidding(
                biddingType = BiddingType.SALE,
                price = 2000,
                product = product,
                user = user
            )
        )
        biddingRepository.save(
            makeBidding(
                biddingType = BiddingType.PURCHASE,
                price = 2000,
                product = product,
                user = user
            )
        )
        val mostExpensiveBidding = biddingRepository.save(
            makeBidding(
                biddingType = BiddingType.SALE,
                price = 3000,
                product = product,
                user = user
            )
        )

        // When
        val sut = biddingRepository.findMostExpensiveBidding(productId = product.id, biddingType = BiddingType.SALE)

        // Then
        sut.shouldNotBeNull()
        sut.id shouldBe mostExpensiveBidding.id
        sut.biddingType shouldBe mostExpensiveBidding.biddingType
        sut.price shouldBe mostExpensiveBidding.price
        sut.status shouldBe mostExpensiveBidding.status
        sut.size shouldBe mostExpensiveBidding.size
        sut.product.id shouldBe mostExpensiveBidding.product.id
        sut.user.id shouldBe mostExpensiveBidding.user.id
    }

    @Test
    fun `특정 상품에 대해 가장 저렴한 입찰을 조회한다`() {
        // Given
        val user = userRepository.save(makeUser(id = 100L))
        val product = productRepository.save(makeProduct(id = 1L))
        val mostCheapestBidding = biddingRepository.save(
            makeBidding(
                biddingType = BiddingType.SALE,
                price = 1000,
                product = product,
                user = user,
            )
        )
        biddingRepository.save(
            makeBidding(
                biddingType = BiddingType.SALE,
                price = 2000,
                product = product,
                user = user,
            )
        )
        biddingRepository.save(
            makeBidding(
                biddingType = BiddingType.PURCHASE,
                price = 2000,
                product = product,
                user = user,
            )
        )
        biddingRepository.save(
            makeBidding(
                biddingType = BiddingType.SALE,
                price = 3000,
                product = product,
                user = user
            )
        )

        // When
        val sut = biddingRepository.findMostCheapestBidding(productId = product.id, biddingType = BiddingType.SALE)

        // Then
        sut.shouldNotBeNull()
        sut.id shouldBe mostCheapestBidding.id
        sut.biddingType shouldBe mostCheapestBidding.biddingType
        sut.price shouldBe mostCheapestBidding.price
        sut.status shouldBe mostCheapestBidding.status
        sut.size shouldBe mostCheapestBidding.size
        sut.product.id shouldBe mostCheapestBidding.product.id
        sut.user.id shouldBe mostCheapestBidding.user.id
    }
}
