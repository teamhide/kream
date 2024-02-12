package com.teamhide.kream.product.adapter.`in`.api.v1

import com.fasterxml.jackson.module.kotlin.readValue
import com.ninjasquad.springmockk.MockkBean
import com.teamhide.kream.client.pg.AttemptPaymentResponse
import com.teamhide.kream.client.pg.PgClient
import com.teamhide.kream.common.outbox.AggregateType
import com.teamhide.kream.common.outbox.OutboxRepository
import com.teamhide.kream.product.adapter.out.persistence.jpa.BiddingRepository
import com.teamhide.kream.product.adapter.out.persistence.jpa.OrderRepository
import com.teamhide.kream.product.adapter.out.persistence.jpa.ProductRepository
import com.teamhide.kream.product.adapter.out.persistence.jpa.SaleHistoryRepository
import com.teamhide.kream.product.application.exception.BiddingNotFoundException
import com.teamhide.kream.product.domain.event.BiddingCompletedEvent
import com.teamhide.kream.product.domain.vo.BiddingStatus
import com.teamhide.kream.product.domain.vo.BiddingType
import com.teamhide.kream.product.domain.vo.OrderStatus
import com.teamhide.kream.product.makeBidding
import com.teamhide.kream.product.makeImmediateSaleRequest
import com.teamhide.kream.product.makeProduct
import com.teamhide.kream.support.test.BaseIntegrationTest
import com.teamhide.kream.user.USER_ID_1_TOKEN
import com.teamhide.kream.user.USER_ID_2_TOKEN
import com.teamhide.kream.user.adapter.out.persistence.jpa.UserRepository
import com.teamhide.kream.user.application.exception.UserNotFoundException
import com.teamhide.kream.user.makeUser
import io.kotest.matchers.nulls.shouldNotBeNull
import io.kotest.matchers.shouldBe
import io.mockk.every
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.repository.findByIdOrNull
import org.springframework.http.HttpHeaders
import org.springframework.test.web.servlet.post

private const val URL = "/v1/bid/sale"

class ImmediateSaleV1ControllerTest : BaseIntegrationTest() {
    @Autowired
    lateinit var biddingRepository: BiddingRepository

    @Autowired
    lateinit var userRepository: UserRepository

    @Autowired
    lateinit var productRepository: ProductRepository

    @Autowired
    lateinit var orderRepository: OrderRepository

    @Autowired
    lateinit var saleHistoryRepository: SaleHistoryRepository

    @Autowired
    lateinit var outboxRepository: OutboxRepository

    @MockkBean
    lateinit var pgClient: PgClient

    @Test
    fun `존재하지 않는 입찰인 경우 404를 리턴한다`() {
        // Given
        val request = makeImmediateSaleRequest()
        val exc = BiddingNotFoundException()

        // When, Then
        mockMvc.post(URL) {
            header(HttpHeaders.AUTHORIZATION, "Bearer $USER_ID_1_TOKEN")
            jsonRequest(request)
        }
            .andExpect {
                status { isNotFound() }
                jsonPath("errorCode") { value(exc.errorCode) }
            }
    }

    @Test
    fun `존재하지 않는 유저인 경우 404를 리턴한다`() {
        // Given
        val request = makeImmediateSaleRequest()

        val user = userRepository.save(makeUser(id = 100L))
        val product = productRepository.save(makeProduct(id = 1L))
        val bidding = makeBidding(id = request.biddingId, product = product, user = user)
        biddingRepository.save(bidding)

        val exc = UserNotFoundException()

        // When, Then
        mockMvc.post(URL) {
            header(HttpHeaders.AUTHORIZATION, "Bearer $USER_ID_2_TOKEN")
            jsonRequest(request)
        }
            .andExpect {
                status { isNotFound() }
                jsonPath("errorCode") { value(exc.errorCode) }
            }
    }

    @Test
    fun `즉시 판매에 성공하면 200을 리턴한다`() {
        // Given
        val request = makeImmediateSaleRequest()

        val seller = userRepository.save(makeUser(id = 1L))
        // purchaser
        userRepository.save(makeUser(id = 2L))

        val product = productRepository.save(makeProduct(id = 1L))
        val bidding = makeBidding(
            id = request.biddingId,
            product = product,
            user = seller,
            biddingType = BiddingType.PURCHASE,
            status = BiddingStatus.IN_PROGRESS,
        )
        val savedBidding = biddingRepository.save(bidding)

        val paymentId = "paymentId"
        every { pgClient.attemptPayment(any()) } returns AttemptPaymentResponse(paymentId = paymentId)

        // When, Then
        mockMvc.post(URL) {
            header(HttpHeaders.AUTHORIZATION, "Bearer $USER_ID_2_TOKEN")
            jsonRequest(request)
        }
            .andExpect {
                status { isOk() }
                jsonPath("biddingId") { value(savedBidding.id) }
                jsonPath("price") { value(savedBidding.price) }
            }

        // Bidding이 완료 상태로 변경되었다
        val sut = biddingRepository.findByIdOrNull(id = savedBidding.id)
        sut.shouldNotBeNull()
        sut.status shouldBe BiddingStatus.COMPLETE

        // 판매 내역이 생성되었다
        val saleHistory = saleHistoryRepository.findByBiddingId(biddingId = savedBidding.id)
        saleHistory.shouldNotBeNull()
        saleHistory.bidding.id shouldBe savedBidding.id
        saleHistory.user.id shouldBe seller.id
        saleHistory.price shouldBe savedBidding.price
        saleHistory.size shouldBe savedBidding.size

        // 주문이 생성되었다
        val order = orderRepository.findByBiddingId(biddingId = savedBidding.id)
        order.shouldNotBeNull()
        order.paymentId shouldBe paymentId
        order.bidding.id shouldBe savedBidding.id
        order.user.id shouldBe seller.id
        order.status shouldBe OrderStatus.COMPLETE

        // 아웃박스 테이블에 이벤트가 저장되었다
        val outboxes = outboxRepository.findAll()
        outboxes.size shouldBe 1

        val outbox = outboxes[0]
        outbox.aggregateType shouldBe AggregateType.BIDDING_COMPLETED
        outbox.completedAt shouldBe null

        val payload = objectMapper.readValue<BiddingCompletedEvent>(outbox.payload)
        payload.biddingId shouldBe sut.id
        payload.productId shouldBe product.id
    }
}
