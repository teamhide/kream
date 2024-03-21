package com.teamhide.kream.product.application.service

import com.teamhide.kream.product.application.exception.ProductBrandNotFoundException
import com.teamhide.kream.product.application.exception.ProductCategoryNotFoundException
import com.teamhide.kream.product.domain.model.InvalidReleasePriceException
import com.teamhide.kream.product.domain.repository.ProductBrandRepository
import com.teamhide.kream.product.domain.repository.ProductCategoryRepository
import com.teamhide.kream.product.domain.repository.ProductDisplayRepository
import com.teamhide.kream.product.domain.repository.ProductRepository
import com.teamhide.kream.product.makeProductBrand
import com.teamhide.kream.product.makeProductCategory
import com.teamhide.kream.product.makeRegisterProductCommand
import com.teamhide.kream.support.test.IntegrationTest
import com.teamhide.kream.support.test.MongoDbCleaner
import com.teamhide.kream.support.test.MysqlDbCleaner
import io.kotest.assertions.throwables.shouldThrow
import io.kotest.core.spec.style.BehaviorSpec
import io.kotest.matchers.nulls.shouldNotBeNull
import io.kotest.matchers.shouldBe

@IntegrationTest
internal class RegisterProductServiceTest(
    private val productDisplayRepository: ProductDisplayRepository,
    private val productRepository: ProductRepository,
    private val registerProductService: RegisterProductService,
    private val productBrandRepository: ProductBrandRepository,
    private val productCategoryRepository: ProductCategoryRepository,
) : BehaviorSpec({
    listeners(MysqlDbCleaner(), MongoDbCleaner())

    Given("brandId에 해당하는 브랜드가 없을 때") {
        val command = makeRegisterProductCommand()

        When("상품 등록을 요청하면") {
            Then("예외가 발생한다") {
                shouldThrow<ProductBrandNotFoundException> { registerProductService.execute(command = command) }
            }
        }
    }

    Given("categoryId에 해당하는 카테고리가 없을 때") {
        val command = makeRegisterProductCommand()

        val productBrand = makeProductBrand()
        productBrandRepository.save(productBrand)

        When("상품 생성을 요청하면") {
            Then("예외가 발생한다") {
                shouldThrow<ProductCategoryNotFoundException> { registerProductService.execute(command = command) }
            }
        }
    }

    Given("상품 가격이 0원 미만일 때") {
        val command = makeRegisterProductCommand(releasePrice = -1)

        val productBrand = makeProductBrand()
        productBrandRepository.save(productBrand)

        val productCategory = makeProductCategory()
        productCategoryRepository.save(productCategory)

        When("상품 등록을 요청하면") {
            Then("예외가 발생한다") {
                shouldThrow<InvalidReleasePriceException> { registerProductService.execute(command = command) }
            }
        }
    }

    Given("존재하는 브랜드/카테고리를 대상으로") {
        val productBrand = productBrandRepository.save(makeProductBrand())

        val productCategory = productCategoryRepository.save(makeProductCategory())

        val command = makeRegisterProductCommand(
            name = "test",
            brandId = productBrand.id,
            categoryId = productCategory.id,
        )

        When("상품 등록을 요청하면") {
            val sut = registerProductService.execute(command = command)
            val savedProduct = productRepository.findAll()[0]

            Then("등록된 상품 정보가 리턴된다") {
                sut.id shouldBe savedProduct.id
                sut.name shouldBe command.name
                sut.sizeType shouldBe command.sizeType
                sut.modelNumber shouldBe command.modelNumber
                sut.releasePrice shouldBe command.releasePrice
                sut.brand shouldBe productBrand.name
                sut.category shouldBe productCategory.name
            }

            Then("상품 전시 데이터가 생성된다") {
                val savedProductDisplay = productDisplayRepository.findAll()[0]
                savedProductDisplay.shouldNotBeNull()
                savedProductDisplay.productId shouldBe savedProduct.id
                savedProductDisplay.name shouldBe command.name
                savedProductDisplay.price shouldBe 0
                savedProductDisplay.brand shouldBe productBrand.name
                savedProductDisplay.category shouldBe productCategory.name
            }
        }
    }
})
