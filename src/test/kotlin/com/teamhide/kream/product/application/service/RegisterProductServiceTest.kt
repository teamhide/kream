package com.teamhide.kream.product.application.service

import com.teamhide.kream.product.adapter.out.persistence.ProductRepositoryAdapter
import com.teamhide.kream.product.application.exception.ProductBrandNotFoundException
import com.teamhide.kream.product.application.exception.ProductCategoryNotFoundException
import com.teamhide.kream.product.domain.model.InvalidReleasePriceException
import com.teamhide.kream.product.makeProduct
import com.teamhide.kream.product.makeProductBrand
import com.teamhide.kream.product.makeProductCategory
import com.teamhide.kream.product.makeRegisterProductCommand
import io.kotest.assertions.throwables.shouldThrow
import io.kotest.core.spec.style.BehaviorSpec
import io.kotest.matchers.shouldBe
import io.mockk.every
import io.mockk.mockk

class RegisterProductServiceTest : BehaviorSpec({
    val productRepositoryAdapter = mockk<ProductRepositoryAdapter>()
    val registerProductService = RegisterProductService(productRepositoryAdapter = productRepositoryAdapter)

    Given("brandId에 해당하는 브랜드가 없을 때") {
        val command = makeRegisterProductCommand()
        every { productRepositoryAdapter.findBrandById(any()) } returns null

        When("상품 생성을 요청하면") {
            Then("예외가 발생한다") {
                shouldThrow<ProductBrandNotFoundException> { registerProductService.execute(command = command) }
            }
        }
    }

    Given("categoryId에 해당하는 카테고리가 없을 때") {
        val command = makeRegisterProductCommand()
        val productBrand = makeProductBrand()
        every { productRepositoryAdapter.findBrandById(any()) } returns productBrand
        every { productRepositoryAdapter.findCategoryById(any()) } returns null

        When("상품 생성을 요청하면") {
            Then("예외가 발생한다") {
                shouldThrow<ProductCategoryNotFoundException> { registerProductService.execute(command = command) }
            }
        }
    }

    Given("상품 가격이 0원 미만일 때") {
        val command = makeRegisterProductCommand(releasePrice = -1)
        val productBrand = makeProductBrand()
        val productCategory = makeProductCategory()
        every { productRepositoryAdapter.findBrandById(any()) } returns productBrand
        every { productRepositoryAdapter.findCategoryById(any()) } returns productCategory

        When("상품 생성을 요청하면") {
            Then("예외가 발생한다") {
                shouldThrow<InvalidReleasePriceException> { registerProductService.execute(command = command) }
            }
        }
    }

    Given("존재하는 브랜드/카테고리를 대상으로") {
        val command = makeRegisterProductCommand()
        val product = makeProduct()
        val productBrand = makeProductBrand()
        val productCategory = makeProductCategory()
        every { productRepositoryAdapter.findBrandById(any()) } returns productBrand
        every { productRepositoryAdapter.findCategoryById(any()) } returns productCategory
        every { productRepositoryAdapter.saveProduct(any()) } returns product

        When("상품 생성을 요청하면") {
            val sut = registerProductService.execute(command = command)

            Then("상품이 생성된다") {
                sut.id shouldBe product.id
                sut.name shouldBe product.name
                sut.sizeType shouldBe product.sizeType
                sut.modelNumber shouldBe product.modelNumber
                sut.releasePrice shouldBe product.releasePrice
                sut.brand shouldBe productBrand.name
                sut.category shouldBe productCategory.name
            }
        }
    }
})
