package com.teamhide.kream.delivery.adapter.out.external

import com.teamhide.kream.product.application.exception.BiddingNotFoundException
import com.teamhide.kream.product.application.exception.ProductNotFoundException
import com.teamhide.kream.product.domain.usecase.GetBiddingByIdUseCase
import com.teamhide.kream.product.domain.usecase.GetProductByIdUseCase
import com.teamhide.kream.product.makeBidding
import com.teamhide.kream.product.makeProduct
import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.nulls.shouldNotBeNull
import io.kotest.matchers.shouldBe
import io.mockk.every
import io.mockk.mockk

class ProductAdapterTest : StringSpec({
    val getProductByIdUseCase = mockk<GetProductByIdUseCase>()
    val getBiddingByIdUseCase = mockk<GetBiddingByIdUseCase>()
    val productAdapter = ProductAdapter(
        getProductByIdUseCase = getProductByIdUseCase,
        getBiddingByIdUseCase = getBiddingByIdUseCase,
    )

    "id로 Product를 조회할 때 예외가 발생하면 null을 리턴한다" {
        // Given
        every { getProductByIdUseCase.execute(any()) } throws ProductNotFoundException()

        // When
        val sut = productAdapter.findProductById(productId = 1L)

        // Then
        sut shouldBe null
    }

    "id로 Product를 조회한다" {
        // Given
        val product = makeProduct()
        every { getProductByIdUseCase.execute(any()) } returns product

        // When
        val sut = productAdapter.findProductById(productId = product.id)

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

    "id로 Bidding을 조회할 때 예외가 발생하면 null을 리턴한다" {
        // Given
        every { getBiddingByIdUseCase.execute(any()) } throws BiddingNotFoundException()

        // When
        val sut = productAdapter.findBiddingById(biddingId = 1L)

        // Then
        sut shouldBe null
    }

    "id로 Bidding을 조회한다" {
        // Given
        val bidding = makeBidding(id = 1L)
        every { getBiddingByIdUseCase.execute(any()) } returns bidding

        // When
        val sut = productAdapter.findBiddingById(biddingId = bidding.id)

        // Then
        sut.shouldNotBeNull()
        sut.product shouldBe bidding.product
        sut.user shouldBe bidding.user
        sut.price shouldBe bidding.price
        sut.size shouldBe bidding.size
        sut.status shouldBe bidding.status
        sut.biddingType shouldBe bidding.biddingType
    }
})
