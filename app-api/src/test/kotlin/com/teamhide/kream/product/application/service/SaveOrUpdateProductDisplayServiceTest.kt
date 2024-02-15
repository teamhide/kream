package com.teamhide.kream.product.application.service

import com.teamhide.kream.product.adapter.out.persistence.ProductDisplayRepositoryAdapter
import com.teamhide.kream.product.adapter.out.persistence.ProductRepositoryAdapter
import com.teamhide.kream.product.domain.model.ProductDisplay
import com.teamhide.kream.product.makeProduct
import com.teamhide.kream.product.makeProductBrand
import com.teamhide.kream.product.makeProductCategory
import com.teamhide.kream.product.makeProductDisplay
import com.teamhide.kream.product.makeSaveOrUpdateProductDisplayCommand
import io.kotest.core.spec.IsolationMode
import io.kotest.core.spec.style.BehaviorSpec
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify

internal class SaveOrUpdateProductDisplayServiceTest : BehaviorSpec({
    isolationMode = IsolationMode.InstancePerLeaf

    val productDisplayRepositoryAdapter = mockk<ProductDisplayRepositoryAdapter>()
    val productRepositoryAdapter = mockk<ProductRepositoryAdapter>()
    val saveOrUpdateProductDisplayService = SaveOrUpdateProductDisplayService(
        productDisplayRepositoryAdapter = productDisplayRepositoryAdapter,
        productRepositoryAdapter = productRepositoryAdapter,
    )

    Given("전시 상품 정보가 존재하지 않고 상품도 존재하지 않을 때") {
        every { productDisplayRepositoryAdapter.findByProductId(any()) } returns null
        every { productRepositoryAdapter.findById(any()) } returns null
        val command = makeSaveOrUpdateProductDisplayCommand()

        When("저장 또는 업데이트 요청을 하면") {
            saveOrUpdateProductDisplayService.execute(command = command)

            Then("저장하지 않는다") {
                verify(exactly = 0) { productDisplayRepositoryAdapter.save(any<ProductDisplay>()) }
            }
        }
    }

    Given("전시 상품 정보가 존재하지 않고 브랜드도 존재하지 않을 때") {
        every { productDisplayRepositoryAdapter.findByProductId(any()) } returns null

        val product = makeProduct()
        every { productRepositoryAdapter.findById(any()) } returns product

        every { productRepositoryAdapter.findBrandById(any()) } returns null
        val command = makeSaveOrUpdateProductDisplayCommand()

        When("저장 또는 업데이트 요청을 하면") {
            saveOrUpdateProductDisplayService.execute(command = command)

            Then("저장하지 않는다") {
                verify(exactly = 0) { productDisplayRepositoryAdapter.save(any<ProductDisplay>()) }
            }
        }
    }

    Given("전시 상품 정보가 존재하지 않고 카테고리도 존재하지 않을 때") {
        every { productDisplayRepositoryAdapter.findByProductId(any()) } returns null

        val product = makeProduct()
        every { productRepositoryAdapter.findById(any()) } returns product

        val brand = makeProductBrand()
        every { productRepositoryAdapter.findBrandById(any()) } returns brand

        every { productRepositoryAdapter.findCategoryById(any()) } returns null

        val command = makeSaveOrUpdateProductDisplayCommand()

        When("저장 또는 업데이트 요청을 하면") {
            saveOrUpdateProductDisplayService.execute(command = command)

            Then("저장하지 않는다") {
                verify(exactly = 0) { productDisplayRepositoryAdapter.save(any<ProductDisplay>()) }
            }
        }
    }

    Given("전시 상품 정보가 존재하지 않을 때") {
        every { productDisplayRepositoryAdapter.findByProductId(any()) } returns null

        val product = makeProduct()
        every { productRepositoryAdapter.findById(any()) } returns product

        val brand = makeProductBrand()
        every { productRepositoryAdapter.findBrandById(any()) } returns brand

        val category = makeProductCategory()
        every { productRepositoryAdapter.findCategoryById(any()) } returns category

        val productDisplay = makeProductDisplay()
        every { productDisplayRepositoryAdapter.save(any()) } returns productDisplay

        val command = makeSaveOrUpdateProductDisplayCommand()

        When("저장 또는 업데이트 요청을 하면") {
            saveOrUpdateProductDisplayService.execute(command = command)

            Then("전시 상품 정보를 저장한다") {
                verify(exactly = 1) { productDisplayRepositoryAdapter.save(any<ProductDisplay>()) }
            }
        }
    }

    Given("전시 상품 정보가 존재하지만 새로운 가격보다 저렴한 경우") {
        val productDisplay = makeProductDisplay(price = 10000)
        every { productDisplayRepositoryAdapter.findByProductId(any()) } returns productDisplay

        val command = makeSaveOrUpdateProductDisplayCommand(price = 20000)

        When("저장 또는 업데이트 요청을 하면") {
            saveOrUpdateProductDisplayService.execute(command = command)

            Then("전시 상품 정보를 업데이트 하지않는다") {
                verify(exactly = 0) { productDisplayRepositoryAdapter.save(any()) }
            }
        }
    }

    Given("전시 상품 정보가 존재하고 새로운 가격보다 저렴한 경우") {
        val productDisplay = makeProductDisplay(price = 30000)
        every { productDisplayRepositoryAdapter.findByProductId(any()) } returns productDisplay
        every { productDisplayRepositoryAdapter.save(any()) } returns productDisplay

        val command = makeSaveOrUpdateProductDisplayCommand(price = 20000)

        When("저장 또는 업데이트 요청을 하면") {
            saveOrUpdateProductDisplayService.execute(command = command)

            Then("전시 상품 정보를 업데이트한다") {
                verify(exactly = 1) { productDisplayRepositoryAdapter.save(any()) }
            }
        }
    }
})
