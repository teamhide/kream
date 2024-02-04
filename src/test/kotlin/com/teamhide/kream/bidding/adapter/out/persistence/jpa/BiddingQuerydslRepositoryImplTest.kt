package com.teamhide.kream.bidding.adapter.out.persistence.jpa

import com.teamhide.kream.bidding.domain.vo.BiddingType
import com.teamhide.kream.bidding.makeBidding
import com.teamhide.kream.product.adapter.out.persistence.jpa.ProductRepository
import com.teamhide.kream.product.makeProduct
import com.teamhide.kream.support.test.JpaRepositoryTest
import com.teamhide.kream.user.adapter.out.persistence.jpa.UserRepository
import com.teamhide.kream.user.makeUser
import io.kotest.matchers.nulls.shouldNotBeNull
import io.kotest.matchers.shouldBe
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired

@JpaRepositoryTest
class BiddingQuerydslRepositoryImplTest {
    @Autowired
    lateinit var biddingRepository: BiddingRepository

    @Autowired
    lateinit var userRepository: UserRepository

    @Autowired
    lateinit var productRepository: ProductRepository

    @Test
    fun `가장 비싼 입찰을 조회한다`() {
        // Given
        val user = userRepository.save(makeUser(id = 100L))
        val product = productRepository.save(makeProduct(id = 1L))
        biddingRepository.save(makeBidding(biddingType = BiddingType.SALE, price = 1000, product = product, user = user))
        biddingRepository.save(makeBidding(biddingType = BiddingType.SALE, price = 2000, product = product, user = user))
        val mostExpensiveBidding = biddingRepository.save(
            makeBidding(
                biddingType = BiddingType.SALE,
                price = 3000,
                product = product,
                user = user
            )
        )

        // When
        val sut = biddingRepository.findMostExpensiveBidding(biddingType = BiddingType.SALE)

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
}
