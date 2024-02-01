package com.teamhide.kream.product.adapter.`in`.api.v1

import com.teamhide.kream.product.adapter.out.persistence.jpa.ProductBrandRepository
import com.teamhide.kream.product.adapter.out.persistence.jpa.ProductCategoryRepository
import com.teamhide.kream.product.adapter.out.persistence.jpa.ProductRepository
import com.teamhide.kream.product.application.exception.ProductBrandNotFoundException
import com.teamhide.kream.product.application.exception.ProductCategoryNotFoundException
import com.teamhide.kream.product.domain.model.InvalidReleasePriceException
import com.teamhide.kream.product.makeProductBrand
import com.teamhide.kream.product.makeProductCategory
import com.teamhide.kream.product.makeRegisterProductRequest
import com.teamhide.kream.support.test.BaseIntegrationTest
import com.teamhide.kream.user.USER_ID_1_TOKEN
import io.kotest.matchers.shouldBe
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.repository.findByIdOrNull
import org.springframework.http.HttpHeaders
import org.springframework.test.web.servlet.post

const val URL = "/v1/product"

class RegisterProductV1ControllerTest : BaseIntegrationTest() {
    @Autowired
    lateinit var productRepository: ProductRepository

    @Autowired
    lateinit var productCategoryRepository: ProductCategoryRepository

    @Autowired
    lateinit var productBrandRepository: ProductBrandRepository

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

        val product = productRepository.findByIdOrNull(1)!!
        product.name shouldBe request.name
        product.sizeType shouldBe request.sizeType
        product.modelNumber shouldBe request.modelNumber
        product.releasePrice shouldBe request.releasePrice
    }
}
