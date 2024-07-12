package com.teamhide.kream.product.ui.api

import com.ninjasquad.springmockk.MockkBean
import com.teamhide.kream.product.domain.usecase.GetProductsUseCase
import com.teamhide.kream.product.domain.usecase.RegisterProductResponseDto
import com.teamhide.kream.product.domain.usecase.RegisterProductUseCase
import com.teamhide.kream.product.domain.vo.SizeType
import com.teamhide.kream.product.makeProductDisplayRead
import com.teamhide.kream.product.makeRegisterProductRequest
import com.teamhide.kream.product.ui.api.dto.RegisterProductResponse
import com.teamhide.kream.support.test.RestControllerTest
import com.teamhide.kream.user.USER_ID_1_TOKEN
import io.mockk.every
import org.junit.jupiter.api.Test
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.http.HttpHeaders
import org.springframework.test.web.servlet.get
import org.springframework.test.web.servlet.post

private const val URL = "/v1/product"

@WebMvcTest(ProductV1Controller::class)
internal class ProductV1ControllerTest : RestControllerTest() {
    @MockkBean
    private lateinit var getProductsUseCase: GetProductsUseCase

    @MockkBean
    private lateinit var registerProductUseCase: RegisterProductUseCase

    @Test
    fun `상품 전시 목록 조회 API`() {
        // Given
        val productDisplay1 = makeProductDisplayRead(
            productId = 1L,
            name = "name1",
            price = 20000,
            brand = "NIKE",
            category = "SHOES",
        )
        val productDisplay2 = makeProductDisplayRead(
            productId = 2L,
            name = "name2",
            price = 30000,
            brand = "MONCLER",
            category = "CLOTHES",
        )
        val products = listOf(productDisplay1, productDisplay2)
        every { getProductsUseCase.execute(any()) } returns products

        // When, Then
        mockMvc.get("$URL?page=0&size=20") {
            header(HttpHeaders.AUTHORIZATION, "Bearer $USER_ID_1_TOKEN")
        }.andExpect {
            status { isOk() }
            jsonResponse(products)
        }
    }

    @Test
    fun `상품을 등록한다`() {
        // Given
        val responseDto = RegisterProductResponseDto(
            id = 1L,
            name = "name",
            releasePrice = 20000,
            modelNumber = "A123",
            sizeType = SizeType.CLOTHES,
            brand = "NIKE",
            category = "SHOES",
        )
        every { registerProductUseCase.execute(any()) } returns responseDto
        val request = makeRegisterProductRequest()
        val response = RegisterProductResponse(
            id = responseDto.id,
            name = responseDto.name,
            releasePrice = responseDto.releasePrice,
            modelNumber = responseDto.modelNumber,
            sizeType = responseDto.sizeType,
            brand = responseDto.brand,
            category = responseDto.category,
        )

        // When, Then
        mockMvc.post(URL) {
            header(HttpHeaders.AUTHORIZATION, "Bearer $USER_ID_1_TOKEN")
            jsonRequest(request)
        }
            .andExpect {
                status { isOk() }
                jsonResponse(response)
            }
    }
}
