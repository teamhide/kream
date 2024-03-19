package com.teamhide.kream.product.ui.api

import com.teamhide.kream.product.application.exception.ProductBrandNotFoundException
import com.teamhide.kream.product.application.exception.ProductCategoryNotFoundException
import com.teamhide.kream.product.domain.model.InvalidReleasePriceException
import com.teamhide.kream.product.domain.repository.ProductBrandRepository
import com.teamhide.kream.product.domain.repository.ProductCategoryRepository
import com.teamhide.kream.product.domain.repository.ProductDisplayRepository
import com.teamhide.kream.product.domain.repository.ProductRepository
import com.teamhide.kream.product.makeProductBrand
import com.teamhide.kream.product.makeProductCategory
import com.teamhide.kream.product.makeProductDisplay
import com.teamhide.kream.product.makeRegisterProductRequest
import com.teamhide.kream.support.test.BaseIntegrationTest
import com.teamhide.kream.user.USER_ID_1_TOKEN
import io.kotest.matchers.nulls.shouldNotBeNull
import io.kotest.matchers.shouldBe
import org.junit.jupiter.api.Test
import org.springframework.data.repository.findByIdOrNull
import org.springframework.http.HttpHeaders
import org.springframework.test.web.servlet.get
import org.springframework.test.web.servlet.post

private const val URL = "/v1/product"

internal class ProductV1ControllerTest(
    private val productDisplayRepository: ProductDisplayRepository,
    private val productRepository: ProductRepository,
    private val productCategoryRepository: ProductCategoryRepository,
    private val productBrandRepository: ProductBrandRepository,
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

    @Test
    fun `존재하지 않는 브랜드인 경우 404를 리턴한다`() {
        // Given
        val request = makeRegisterProductRequest()
        val exc = ProductBrandNotFoundException()

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
    fun `존재하지 않는 카테고리인 경우 404를 리턴한다`() {
        // Given
        val request = makeRegisterProductRequest()
        val productBrand = makeProductBrand(id = request.brandId)
        productBrandRepository.save(productBrand)
        val exc = ProductCategoryNotFoundException()

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
    fun `상품 가격이 0원 미만이라면 400을 리턴한다`() {
        // Given
        val request = makeRegisterProductRequest(releasePrice = 0)
        val productBrand = makeProductBrand(id = request.brandId)
        val productCategory = makeProductCategory(id = request.categoryId)
        productBrandRepository.save(productBrand)
        productCategoryRepository.save(productCategory)
        val exc = InvalidReleasePriceException()

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
    fun `상품을 등록한다`() {
        // Given
        val request = makeRegisterProductRequest()
        val productBrand = makeProductBrand(id = request.brandId)
        val productCategory = makeProductCategory(id = request.categoryId)
        productBrandRepository.save(productBrand)
        productCategoryRepository.save(productCategory)

        // When, Then
        mockMvc.post(URL) {
            header(HttpHeaders.AUTHORIZATION, "Bearer $USER_ID_1_TOKEN")
            jsonRequest(request)
        }
            .andExpect {
                status { isOk() }
                jsonPath("id") { value(1) }
                jsonPath("releasePrice") { value(request.releasePrice) }
                jsonPath("modelNumber") { value(request.modelNumber) }
                jsonPath("sizeType") { value(request.sizeType.name) }
                jsonPath("brand") { value(productBrand.name) }
                jsonPath("category") { value(productCategory.name) }
            }

        val product = productRepository.findByIdOrNull(1)
        product.shouldNotBeNull()
        product.name shouldBe request.name
        product.sizeType shouldBe request.sizeType
        product.modelNumber shouldBe request.modelNumber
        product.releasePrice shouldBe request.releasePrice

        val productDisplay = productDisplayRepository.findByProductId(productId = product.id)
        productDisplay.shouldNotBeNull()
        productDisplay.productId shouldBe product.id
        productDisplay.name shouldBe product.name
        productDisplay.price shouldBe 0
        productDisplay.brand shouldBe productBrand.name
        productDisplay.category shouldBe productCategory.name
    }
}
