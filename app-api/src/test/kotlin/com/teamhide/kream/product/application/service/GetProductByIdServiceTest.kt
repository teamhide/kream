package com.teamhide.kream.product.application.service

import com.teamhide.kream.product.adapter.out.persistence.ProductRepositoryAdapter
import com.teamhide.kream.product.application.exception.ProductNotFoundException
import com.teamhide.kream.product.domain.usecase.GetProductByIdQuery
import com.teamhide.kream.product.makeProduct
import io.kotest.assertions.throwables.shouldThrow
import io.kotest.core.spec.style.BehaviorSpec
import io.kotest.matchers.nulls.shouldNotBeNull
import io.kotest.matchers.shouldBe
import io.mockk.every
import io.mockk.mockk

class GetProductByIdServiceTest : BehaviorSpec({
    val productRepositoryAdapter = mockk<ProductRepositoryAdapter>()
    val getProductByIdService = GetProductByIdService(productRepositoryAdapter = productRepositoryAdapter)

    Given("존재하지 않는 Product를") {
        val query = GetProductByIdQuery(productId = 1L)
        every { productRepositoryAdapter.findById(any()) } returns null

        When("조회 요청하면") {
            Then("예외가 발생한다") {
                shouldThrow<ProductNotFoundException> { getProductByIdService.execute(query = query) }
            }
        }
    }

    Given("존재하는 Product를") {
        val product = makeProduct(id = 1L)
        val query = GetProductByIdQuery(productId = product.id)
        every { productRepositoryAdapter.findById(any()) } returns product

        When("조회 요청하면") {
            val sut = getProductByIdService.execute(query = query)

            Then("Product를 리턴한다") {
                sut.shouldNotBeNull()
                sut.id shouldBe product.id
                sut.name shouldBe product.name
                sut.releasePrice shouldBe product.releasePrice
                sut.modelNumber shouldBe product.modelNumber
                sut.sizeType shouldBe product.sizeType
                sut.productBrand shouldBe product.productBrand
                sut.productCategory shouldBe product.productCategory
            }
        }
    }
})
