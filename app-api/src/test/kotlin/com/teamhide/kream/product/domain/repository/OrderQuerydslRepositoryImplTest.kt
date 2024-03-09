package com.teamhide.kream.product.domain.repository

import com.teamhide.kream.product.makeBidding
import com.teamhide.kream.product.makeOrder
import com.teamhide.kream.product.makeProduct
import com.teamhide.kream.support.test.JpaRepositoryTest
import com.teamhide.kream.user.domain.repository.UserRepository
import com.teamhide.kream.user.makeUser
import io.kotest.matchers.nulls.shouldNotBeNull
import io.kotest.matchers.shouldBe
import org.junit.jupiter.api.Test

@JpaRepositoryTest
internal class OrderQuerydslRepositoryImplTest(
    private val orderRepository: OrderRepository,
    private val biddingRepository: BiddingRepository,
    private val productRepository: ProductRepository,
    private val userRepository: UserRepository,
) {
    @Test
    fun `biddingId로 Order를 조회한다`() {
        // Given
        val user = userRepository.save(makeUser())
        val product = productRepository.save(makeProduct())
        val bidding = biddingRepository.save(makeBidding(product = product, user = user))
        val order = makeOrder(bidding = bidding, user = user)
        val savedOrder = orderRepository.save(order)

        // When
        val sut = orderRepository.findByBiddingId(biddingId = bidding.id)

        // Then
        sut.shouldNotBeNull()
        sut.paymentId shouldBe savedOrder.paymentId
        sut.bidding.id shouldBe bidding.id
        sut.user.id shouldBe user.id
        sut.status shouldBe savedOrder.status
    }
}
