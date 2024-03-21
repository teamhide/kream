package com.teamhide.kream.product.ui.api

import com.ninjasquad.springmockk.MockkBean
import com.teamhide.kream.product.domain.usecase.BidResponseDto
import com.teamhide.kream.product.domain.usecase.BidUseCase
import com.teamhide.kream.product.domain.vo.BiddingType
import com.teamhide.kream.product.makeBidRequest
import com.teamhide.kream.support.test.RestControllerTest
import com.teamhide.kream.user.USER_ID_1_TOKEN
import io.mockk.every
import org.junit.jupiter.api.Test
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.http.HttpHeaders
import org.springframework.test.web.servlet.post

private const val URL = "/v1/bid"

@WebMvcTest(BidV1Controller::class)
internal class BidV1ControllerTest : RestControllerTest() {
    @MockkBean
    private lateinit var bidUseCase: BidUseCase

    @Test
    fun `입찰 API`() {
        // Given
        val request = makeBidRequest()
        val bidResponseDto = BidResponseDto(
            biddingId = 1L,
            price = 10000,
            size = "250",
            biddingType = BiddingType.SALE,
        )
        val response = BidResponse(
            biddingId = 1L,
            price = 10000,
            size = "250",
            biddingType = BiddingType.SALE,
        )
        every { bidUseCase.execute(any()) } returns bidResponseDto

        // When, Then
        mockMvc.post(URL) {
            header(HttpHeaders.AUTHORIZATION, "Bearer $USER_ID_1_TOKEN")
            jsonRequest(request)
        }.andExpect {
            status { isOk() }
            jsonResponse(response)
        }
    }
}
