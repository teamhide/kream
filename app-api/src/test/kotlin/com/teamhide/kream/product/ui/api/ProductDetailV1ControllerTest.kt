package com.teamhide.kream.product.ui.api

import com.teamhide.kream.product.application.exception.ProductNotFoundException
import com.teamhide.kream.product.domain.repository.BiddingRepository
import com.teamhide.kream.product.domain.repository.ProductBrandRepository
import com.teamhide.kream.product.domain.repository.ProductCategoryRepository
import com.teamhide.kream.product.domain.repository.ProductRepository
import com.teamhide.kream.product.domain.vo.BiddingType
import com.teamhide.kream.product.makeBidding
import com.teamhide.kream.product.makeProduct
import com.teamhide.kream.product.makeProductBrand
import com.teamhide.kream.product.makeProductCategory
import com.teamhide.kream.support.test.BaseIntegrationTest
import com.teamhide.kream.user.USER_ID_1_TOKEN
import com.teamhide.kream.user.domain.repository.UserRepository
import com.teamhide.kream.user.makeUser
import org.junit.jupiter.api.Test
import org.springframework.http.HttpHeaders
import org.springframework.test.web.servlet.get

private const val URL = "/v1/product"

internal class ProductDetailV1ControllerTest(
    private val productRepository: ProductRepository,
    private val productBrandRepository: ProductBrandRepository,
    private val productCategoryRepository: ProductCategoryRepository,
    private val biddingRepository: BiddingRepository,
    private val userRepository: UserRepository,
) : BaseIntegrationTest() {
    @Test
    fun `상품이 존재하지 않으면 404가 리턴된다`() {
        // Given
        val exc = ProductNotFoundException()

        // When, Then
        mockMvc.get("$URL/1") {
            header(HttpHeaders.AUTHORIZATION, "Bearer $USER_ID_1_TOKEN")
        }.andExpect {
            status { isNotFound() }
        }.andExpect {
            jsonPath("errorCode") { value(exc.errorCode) }
        }
    }

    @Test
    fun `상품 상세 정보를 조회한다`() {
        // Given
        val productCategory = makeProductCategory()
        productCategoryRepository.save(productCategory)

        val productBrand = makeProductBrand()
        productBrandRepository.save(productBrand)

        val product = makeProduct(productCategory = productCategory, productBrand = productBrand)
        productRepository.save(product)

        val user = makeUser()
        userRepository.save(user)

        val mostCheapestBidding = makeBidding(price = 2000, biddingType = BiddingType.SALE, product = product, user = user)
        biddingRepository.save(mostCheapestBidding)

        val mostExpensiveBidding = makeBidding(price = 100000, biddingType = BiddingType.PURCHASE, product = product, user = user)
        biddingRepository.save(mostExpensiveBidding)

        // When, Then
        mockMvc.get("$URL/1") {
            header(HttpHeaders.AUTHORIZATION, "Bearer $USER_ID_1_TOKEN")
        }.andExpect {
            status { isOk() }
        }.andExpect {
            jsonPath("productId") { value(product.id) }
            jsonPath("releasePrice") { value(product.releasePrice) }
            jsonPath("modelNumber") { value(product.modelNumber) }
            jsonPath("name") { value(product.name) }
            jsonPath("brand") { value(productBrand.name) }
            jsonPath("category") { value(productCategory.name) }
            jsonPath("purchaseBidPrice") { value(mostCheapestBidding.price) }
            jsonPath("saleBidPrice") { value(mostExpensiveBidding.price) }
        }
    }
}
