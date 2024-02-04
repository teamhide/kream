package com.teamhide.kream.product.adapter.out.persistence

import com.teamhide.kream.product.adapter.out.persistence.mongo.ProductDisplayRepository
import com.teamhide.kream.product.domain.model.ProductDisplay
import com.teamhide.kream.product.makeProductDisplay
import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.nulls.shouldNotBeNull
import io.kotest.matchers.shouldBe
import io.mockk.every
import io.mockk.mockk

class ProductDisplayRepositoryAdapterTest : StringSpec({
    val productDisplayRepository = mockk<ProductDisplayRepository>()
    val productDisplayRepositoryAdapter =
        ProductDisplayRepositoryAdapter(productDisplayRepository = productDisplayRepository)

    "productId로 ProductDisplay를 조회한다" {
        // Given
        val productId = 1L
        val productDisplay = makeProductDisplay()
        every { productDisplayRepository.findByProductId(any()) } returns productDisplay

        // When
        val sut = productDisplayRepositoryAdapter.findByProductId(productId = productId)

        // Then
        sut.shouldNotBeNull()
        sut.productId shouldBe productDisplay.productId
        sut.name shouldBe productDisplay.name
        sut.price shouldBe productDisplay.price
        sut.brand shouldBe productDisplay.brand
        sut.category shouldBe productDisplay.category
    }

    "ProductDisplay를 저장한다" {
        // Given
        val productDisplay = makeProductDisplay()
        every { productDisplayRepository.save(any<ProductDisplay>()) } returns productDisplay

        // When
        val sut = productDisplayRepositoryAdapter.save(productDisplay = productDisplay)

        // Then
        sut.shouldNotBeNull()
        sut.productId shouldBe productDisplay.productId
        sut.name shouldBe productDisplay.name
        sut.price shouldBe productDisplay.price
        sut.brand shouldBe productDisplay.brand
        sut.category shouldBe productDisplay.category
    }
})
