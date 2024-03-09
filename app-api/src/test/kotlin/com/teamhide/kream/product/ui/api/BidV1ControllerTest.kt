package com.teamhide.kream.product.ui.api

import com.teamhide.kream.product.application.exception.ImmediateTradeAvailableException
import com.teamhide.kream.product.application.exception.ProductNotFoundException
import com.teamhide.kream.product.domain.repository.BiddingRepository
import com.teamhide.kream.product.domain.repository.ProductRepository
import com.teamhide.kream.product.domain.vo.BiddingStatus
import com.teamhide.kream.product.domain.vo.BiddingType
import com.teamhide.kream.product.makeBidRequest
import com.teamhide.kream.product.makeBidding
import com.teamhide.kream.product.makeProduct
import com.teamhide.kream.support.test.BaseIntegrationTest
import com.teamhide.kream.user.USER_ID_1_TOKEN
import com.teamhide.kream.user.domain.repository.UserRepository
import com.teamhide.kream.user.makeUser
import io.kotest.matchers.shouldBe
import org.junit.jupiter.api.Test
import org.springframework.http.HttpHeaders
import org.springframework.test.web.servlet.post

private const val URL = "/v1/bid"

internal class BidV1ControllerTest(
    private val biddingRepository: BiddingRepository,
    private val userRepository: UserRepository,
    private val productRepository: ProductRepository,
) : BaseIntegrationTest() {
    @Test
    fun `판매 입찰 가격이 현재 가장 비싼 구매 가격과 동일한 경우 400을 리턴한다`() {
        // Given
        val request = makeBidRequest(price = 2000, biddingType = BiddingType.SALE)

        val user = userRepository.save(makeUser(id = 1L))
        val product = productRepository.save(makeProduct(id = 1L))
        val bid = makeBidding(price = 2000, user = user, product = product, biddingType = BiddingType.PURCHASE)
        biddingRepository.save(bid)
        val exc = ImmediateTradeAvailableException()

        // When, Then
        mockMvc.post(URL) {
            header(HttpHeaders.AUTHORIZATION, "Bearer $USER_ID_1_TOKEN")
            jsonRequest(request)
        }
            .andExpect {
                status { isBadRequest() }
                jsonPath("errorCode") { value(exc.errorCode) }
            }
    }

    @Test
    fun `판매 입찰 가격이 현재 가장 비싼 구매 가격보다 작은 경우 400을 리턴한다`() {
        // Given
        val request = makeBidRequest(price = 4000, biddingType = BiddingType.SALE)

        val user = userRepository.save(makeUser(id = 1L))
        val product = productRepository.save(makeProduct(id = 1L))
        val bid = makeBidding(price = 5000, user = user, product = product, biddingType = BiddingType.PURCHASE)
        biddingRepository.save(bid)
        val exc = ImmediateTradeAvailableException()

        // When, Then
        mockMvc.post(URL) {
            header(HttpHeaders.AUTHORIZATION, "Bearer $USER_ID_1_TOKEN")
            jsonRequest(request)
        }
            .andExpect {
                status { isBadRequest() }
                jsonPath("errorCode") { value(exc.errorCode) }
            }
    }

    @Test
    fun `구매 입찰 가격이 현재 가장 비싼 판매 가격보다 큰 경우 400을 리턴한다`() {
        // Given
        val request = makeBidRequest(price = 5000, biddingType = BiddingType.PURCHASE)

        val user = userRepository.save(makeUser(id = 1L))
        val product = productRepository.save(makeProduct(id = 1L))
        val bid = makeBidding(price = 4000, user = user, product = product, biddingType = BiddingType.SALE)
        biddingRepository.save(bid)
        val exc = ImmediateTradeAvailableException()

        // When, Then
        mockMvc.post(URL) {
            header(HttpHeaders.AUTHORIZATION, "Bearer $USER_ID_1_TOKEN")
            jsonRequest(request)
        }
            .andExpect {
                status { isBadRequest() }
                jsonPath("errorCode") { value(exc.errorCode) }
            }
    }

    @Test
    fun `존재하지 상품에 입찰하는 경우 404를 리턴한다`() {
        // Given
        val request = makeBidRequest(price = 4000, biddingType = BiddingType.PURCHASE)
        userRepository.save(makeUser(id = 1L))
        val exc = ProductNotFoundException()

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
    fun `입찰에 성공하면 200을 리턴한다`() {
        // Given
        val product = productRepository.save(makeProduct(id = 1L))
        val request = makeBidRequest(price = 4000, biddingType = BiddingType.PURCHASE, productId = product.id)
        userRepository.save(makeUser(id = 1L))

        // When, Then
        mockMvc.post(URL) {
            header(HttpHeaders.AUTHORIZATION, "Bearer $USER_ID_1_TOKEN")
            jsonRequest(request)
        }
            .andExpect {
                status { isOk() }
                jsonPath("biddingId") { value(1) }
            }

        val bidding = biddingRepository.findAll()[0]
        bidding.biddingType shouldBe request.biddingType
        bidding.status shouldBe BiddingStatus.IN_PROGRESS
        bidding.size shouldBe request.size
        bidding.price shouldBe request.price
    }
}
