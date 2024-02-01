package com.teamhide.kream.bidding.adapter.out.persistence.jpa

import com.teamhide.kream.bidding.makeBidding
import com.teamhide.kream.bidding.makeOrder
import com.teamhide.kream.product.adapter.out.persistence.jpa.ProductRepository
import com.teamhide.kream.product.makeProduct
import com.teamhide.kream.support.test.RepositoryTest
import com.teamhide.kream.user.adapter.out.persistence.jpa.UserRepository
import com.teamhide.kream.user.makeUser
import io.kotest.matchers.nulls.shouldNotBeNull
import io.kotest.matchers.shouldBe
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired

@RepositoryTest
class OrderQuerydslRepositoryImplTest {
    @Autowired
    lateinit var orderRepository: OrderRepository

    @Autowired
    lateinit var biddingRepository: BiddingRepository

    @Autowired
    lateinit var productRepository: ProductRepository

    @Autowired
    lateinit var userRepository: UserRepository

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
