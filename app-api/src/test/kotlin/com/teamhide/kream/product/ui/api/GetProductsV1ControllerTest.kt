package com.teamhide.kream.product.ui.api

import com.teamhide.kream.product.domain.repository.ProductDisplayRepository
import com.teamhide.kream.product.makeProductDisplay
import com.teamhide.kream.support.test.BaseIntegrationTest
import com.teamhide.kream.user.USER_ID_1_TOKEN
import org.junit.jupiter.api.Test
import org.springframework.http.HttpHeaders
import org.springframework.test.web.servlet.get

private const val URL = "/v1/product"

internal class GetProductsV1ControllerTest(
    private val productDisplayRepository: ProductDisplayRepository,
) : BaseIntegrationTest() {
    @Test
    fun `상품 전시 목록을 조회한다`() {
        // Given
        val product1 = makeProductDisplay(
            productId = 1L,
            name = "name1",
            price = 20000,
            brand = "NIKE",
            category = "SHOES",
            lastBiddingId = 1L,
        )
        val product2 = makeProductDisplay(
            productId = 2L,
            name = "name2",
            price = 30000,
            brand = "MONCLER",
            category = "CLOTHES",
            lastBiddingId = 2L,
        )
        productDisplayRepository.saveAll(listOf(product1, product2))

        // When, Then
        mockMvc.get("$URL?page=0&size=20") {
            header(HttpHeaders.AUTHORIZATION, "Bearer $USER_ID_1_TOKEN")
        }.andExpect {
            jsonPath("data.[0].productId") { value(product1.productId) }
            jsonPath("data.[0].name") { value(product1.name) }
            jsonPath("data.[0].price") { value(product1.price) }
            jsonPath("data.[0].brand") { value(product1.brand) }
            jsonPath("data.[0].category") { value(product1.category) }

            jsonPath("data.[1].productId") { value(product2.productId) }
            jsonPath("data.[1].name") { value(product2.name) }
            jsonPath("data.[1].price") { value(product2.price) }
            jsonPath("data.[1].brand") { value(product2.brand) }
            jsonPath("data.[1].category") { value(product2.category) }
        }
    }
}
