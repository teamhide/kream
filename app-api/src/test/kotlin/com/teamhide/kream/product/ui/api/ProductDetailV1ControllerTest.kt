package com.teamhide.kream.product.ui.api

import com.ninjasquad.springmockk.MockkBean
import com.teamhide.kream.product.domain.model.ProductDetail
import com.teamhide.kream.product.domain.usecase.GetProductDetailUseCase
import com.teamhide.kream.support.test.RestControllerTest
import com.teamhide.kream.user.USER_ID_1_TOKEN
import io.mockk.every
import org.junit.jupiter.api.Test
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.http.HttpHeaders
import org.springframework.test.web.servlet.get

private const val URL = "/v1/product"

@WebMvcTest(ProductDetailV1Controller::class)
internal class ProductDetailV1ControllerTest : RestControllerTest() {
    @MockkBean
    private lateinit var getProductDetailUseCase: GetProductDetailUseCase

    @Test
    fun `상품 상세 정보 조회 API`() {
        // Given
        val responseDto = ProductDetail(
            productId = 1L,
            releasePrice = 20000,
            modelNumber = "BBB",
            name = "hide",
            brand = "NIKE",
            category = "SHOES",
            purchaseBidPrice = 20000,
            saleBidPrice = 30000,
        )
        val response = GetProductResponse(
            productId = responseDto.productId,
            releasePrice = responseDto.releasePrice,
            modelNumber = responseDto.modelNumber,
            name = responseDto.name,
            brand = responseDto.brand,
            category = responseDto.category,
            purchaseBidPrice = responseDto.purchaseBidPrice,
            saleBidPrice = responseDto.saleBidPrice,
        )
        every { getProductDetailUseCase.execute(any()) } returns responseDto

        // When, Then
        mockMvc.get("$URL/1") {
            header(HttpHeaders.AUTHORIZATION, "Bearer $USER_ID_1_TOKEN")
        }.andExpect {
            status { isOk() }
            jsonResponse(response)
        }
    }
}
