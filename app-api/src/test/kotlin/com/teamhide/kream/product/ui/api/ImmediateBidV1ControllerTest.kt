package com.teamhide.kream.product.ui.api

import com.ninjasquad.springmockk.MockkBean
import com.teamhide.kream.product.domain.usecase.ImmediatePurchaseResponseDto
import com.teamhide.kream.product.domain.usecase.ImmediatePurchaseUseCase
import com.teamhide.kream.product.domain.usecase.ImmediateSaleResponseDto
import com.teamhide.kream.product.domain.usecase.ImmediateSaleUseCase
import com.teamhide.kream.product.makeImmediatePurchaseRequest
import com.teamhide.kream.product.makeImmediateSaleRequest
import com.teamhide.kream.product.ui.api.dto.ImmediatePurchaseResponse
import com.teamhide.kream.product.ui.api.dto.ImmediateSaleResponse
import com.teamhide.kream.support.test.RestControllerTest
import com.teamhide.kream.user.USER_ID_1_TOKEN
import io.mockk.every
import org.junit.jupiter.api.Test
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.http.HttpHeaders
import org.springframework.test.web.servlet.post

private const val URL = "/v1/bid"

@WebMvcTest(ImmediateBidV1Controller::class)
internal class ImmediateBidV1ControllerTest : RestControllerTest() {
    @MockkBean
    lateinit var immediatePurchaseUseCase: ImmediatePurchaseUseCase

    @MockkBean
    lateinit var immediateSaleUseCase: ImmediateSaleUseCase

    @Test
    fun `즉시 구매 API`() {
        // Given
        val request = makeImmediatePurchaseRequest()
        val responseDto = ImmediatePurchaseResponseDto(biddingId = 1L, price = 20000)
        val response = ImmediatePurchaseResponse(biddingId = 1L, price = 20000)
        every { immediatePurchaseUseCase.execute(any()) } returns responseDto

        // When, Then
        mockMvc.post("$URL/purchase") {
            header(HttpHeaders.AUTHORIZATION, "Bearer $USER_ID_1_TOKEN")
            jsonRequest(request)
        }.andExpect {
            status { isOk() }
            jsonResponse(response)
        }
    }

    @Test
    fun `즉시 판매 API`() {
        // Given
        val request = makeImmediateSaleRequest()
        val responseDto = ImmediateSaleResponseDto(biddingId = 1L, price = 20000)
        val response = ImmediateSaleResponse(biddingId = 1L, price = 20000)
        every { immediateSaleUseCase.execute(any()) } returns responseDto

        // When, Then
        mockMvc.post("$URL/sale") {
            header(HttpHeaders.AUTHORIZATION, "Bearer $USER_ID_1_TOKEN")
            jsonRequest(request)
        }.andExpect {
            status { isOk() }
            jsonResponse(response)
        }
    }
}
