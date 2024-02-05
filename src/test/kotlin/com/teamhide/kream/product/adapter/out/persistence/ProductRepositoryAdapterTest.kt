package com.teamhide.kream.product.adapter.out.persistence

import com.teamhide.kream.product.adapter.out.persistence.jpa.ProductBrandRepository
import com.teamhide.kream.product.adapter.out.persistence.jpa.ProductCategoryRepository
import com.teamhide.kream.product.adapter.out.persistence.jpa.ProductRepository
import com.teamhide.kream.product.makeProduct
import com.teamhide.kream.product.makeProductBrand
import com.teamhide.kream.product.makeProductCategory
import com.teamhide.kream.product.makeProductDetailDto
import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.nulls.shouldNotBeNull
import io.kotest.matchers.shouldBe
import io.mockk.every
import io.mockk.mockk
import org.springframework.data.repository.findByIdOrNull

class ProductRepositoryAdapterTest : StringSpec({
    val productRepository = mockk<ProductRepository>()
    val productCategoryRepository = mockk<ProductCategoryRepository>()
    val productBrandRepository = mockk<ProductBrandRepository>()
    val productRepositoryAdapter = ProductRepositoryAdapter(
        productRepository = productRepository,
        productBrandRepository = productBrandRepository,
        productCategoryRepository = productCategoryRepository,
    )

    "id로 Product를 조회한다" {
        // Given
        val id = 1L
        val product = makeProduct()
        every { productRepository.findByIdOrNull(id) } returns product

        // When
        val sut = productRepositoryAdapter.findById(id)

        // Then
        sut.shouldNotBeNull()
        sut.id shouldBe product.id
        sut.name shouldBe product.name
        sut.productCategory shouldBe product.productCategory
        sut.productBrand shouldBe product.productBrand
        sut.releasePrice shouldBe product.releasePrice
        sut.modelNumber shouldBe product.modelNumber
        sut.sizeType shouldBe product.sizeType
    }

    "id로 ProductCategory를 조회한다" {
        // Given
        val id = 1L
        val productCategory = makeProductCategory()
        every { productRepositoryAdapter.findCategoryById(id) } returns productCategory

        // When
        val sut = productRepositoryAdapter.findCategoryById(id)

        // Then
        sut.shouldNotBeNull()
        sut.id shouldBe productCategory.id
        sut.name shouldBe productCategory.name
        sut.parentCategoryId shouldBe productCategory.parentCategoryId
    }

    "id로 ProductBrand를 조회한다" {
        // Given
        val id = 1L
        val productBrand = makeProductBrand()
        every { productRepositoryAdapter.findBrandById(id) } returns productBrand

        // When
        val sut = productRepositoryAdapter.findBrandById(id)

        // Then
        sut.shouldNotBeNull()
        sut.id shouldBe productBrand.id
        sut.name shouldBe productBrand.name
    }

    "id로 ProductInfo를 조회한다" {
        // Given
        val productDetailDto = makeProductDetailDto()
        every { productRepository.findDetailById(any()) } returns productDetailDto

        // When
        val sut = productRepositoryAdapter.findInfoById(productId = 1L)

        // Then
        sut.shouldNotBeNull()
        sut.productId shouldBe productDetailDto.productId
        sut.releasePrice shouldBe productDetailDto.releasePrice
        sut.modelNumber shouldBe productDetailDto.modelNumber
        sut.name shouldBe productDetailDto.name
        sut.brand shouldBe productDetailDto.brand
        sut.category shouldBe productDetailDto.category
    }
})
