package com.teamhide.kream.product.domain.repository

import com.teamhide.kream.product.makeBidding
import com.teamhide.kream.product.makeProduct
import com.teamhide.kream.product.makeSaleHistory
import com.teamhide.kream.support.test.JpaRepositoryTest
import com.teamhide.kream.user.domain.repository.UserRepository
import com.teamhide.kream.user.makeUser
import io.kotest.matchers.nulls.shouldNotBeNull
import io.kotest.matchers.shouldBe
import org.junit.jupiter.api.Test

@JpaRepositoryTest
internal class SaleHistoryQuerydslRepositoryImplTest(
    private val saleHistoryRepository: SaleHistoryRepository,
    private val biddingRepository: BiddingRepository,
    private val productRepository: ProductRepository,
    private val userRepository: UserRepository,
) {
    @Test
    fun `biddingId로 SaleHistory를 조회한다`() {
        // Given
        val user = userRepository.save(makeUser())
        val product = productRepository.save(makeProduct())
        val bidding = biddingRepository.save(makeBidding(product = product, user = user))
        val saleHistory = makeSaleHistory(bidding = bidding, user = user)
        saleHistoryRepository.save(saleHistory)

        // When
        val sut = saleHistoryRepository.findByBiddingId(biddingId = bidding.id)

        // Then
        sut.shouldNotBeNull()
        sut.bidding.id shouldBe bidding.id
        sut.user.id shouldBe user.id
        sut.price shouldBe bidding.price
        sut.size shouldBe bidding.size
    }
}
