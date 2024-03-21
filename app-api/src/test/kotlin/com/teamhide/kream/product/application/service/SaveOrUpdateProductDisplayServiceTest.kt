package com.teamhide.kream.product.application.service

import com.teamhide.kream.product.domain.repository.ProductBrandRepository
import com.teamhide.kream.product.domain.repository.ProductCategoryRepository
import com.teamhide.kream.product.domain.repository.ProductDisplayRepository
import com.teamhide.kream.product.domain.repository.ProductRepository
import com.teamhide.kream.product.makeProduct
import com.teamhide.kream.product.makeProductBrand
import com.teamhide.kream.product.makeProductCategory
import com.teamhide.kream.product.makeProductDisplay
import com.teamhide.kream.product.makeSaveOrUpdateProductDisplayCommand
import com.teamhide.kream.support.test.IntegrationTest
import com.teamhide.kream.support.test.MongoDbCleaner
import com.teamhide.kream.support.test.MysqlDbCleaner
import io.kotest.core.spec.style.BehaviorSpec
import io.kotest.matchers.nulls.shouldNotBeNull
import io.kotest.matchers.shouldBe

@IntegrationTest
internal class SaveOrUpdateProductDisplayServiceTest(
    private val saveOrUpdateProductDisplayService: SaveOrUpdateProductDisplayService,
    private val productRepository: ProductRepository,
    private val productDisplayRepository: ProductDisplayRepository,
    private val productBrandRepository: ProductBrandRepository,
    private val productCategoryRepository: ProductCategoryRepository,
) : BehaviorSpec({
    listeners(MysqlDbCleaner(), MongoDbCleaner())

    Given("전시 상품 정보가 존재하지 않고 상품도 존재하지 않을 때") {
        val command = makeSaveOrUpdateProductDisplayCommand()

        When("저장 또는 업데이트 요청을 하면") {
            saveOrUpdateProductDisplayService.execute(command = command)

            Then("저장하지 않는다") {
                productDisplayRepository.findAll().size shouldBe 0
            }
        }
    }

    Given("전시 상품 정보가 존재하지 않고 브랜드도 존재하지 않을 때") {
        productRepository.save(makeProduct())

        val command = makeSaveOrUpdateProductDisplayCommand()

        When("저장 또는 업데이트 요청을 하면") {
            saveOrUpdateProductDisplayService.execute(command = command)

            Then("저장하지 않는다") {
                productDisplayRepository.findAll().size shouldBe 0
            }
        }
    }

    Given("전시 상품 정보가 존재하지 않고 카테고리도 존재하지 않을 때") {
        productRepository.save(makeProduct())

        productBrandRepository.save(makeProductBrand())

        val command = makeSaveOrUpdateProductDisplayCommand()

        When("저장 또는 업데이트 요청을 하면") {
            saveOrUpdateProductDisplayService.execute(command = command)

            Then("저장하지 않는다") {
                productDisplayRepository.findAll().size shouldBe 0
            }
        }
    }

    Given("전시 상품 정보가 존재하지 않을 때") {
        val productBrand = productBrandRepository.save(makeProductBrand())

        val productCategory = productCategoryRepository.save(makeProductCategory())

        val product = productRepository.save(makeProduct(productBrand = productBrand, productCategory = productCategory))

        val command = makeSaveOrUpdateProductDisplayCommand()

        When("저장 또는 업데이트 요청을 하면") {
            saveOrUpdateProductDisplayService.execute(command = command)

            Then("전시 상품 정보를 저장한다") {
                val sut = productDisplayRepository.findByProductId(productId = product.id)
                sut.shouldNotBeNull()
                sut.productId shouldBe product.id
                sut.name shouldBe product.name
                sut.price shouldBe command.price
                sut.brand shouldBe productBrand.name
                sut.category shouldBe productCategory.name
                sut.lastBiddingId shouldBe command.biddingId
            }
        }
    }

    Given("전시 상품 정보가 존재하지만 새로운 가격보다 저렴한 경우") {
        val productDisplay = productDisplayRepository.save(makeProductDisplay(price = 10000))

        val command = makeSaveOrUpdateProductDisplayCommand(price = 20000)

        When("저장 또는 업데이트 요청을 하면") {
            saveOrUpdateProductDisplayService.execute(command = command)

            Then("전시 상품 정보를 업데이트 하지않는다") {
                val sut = productDisplayRepository.findByProductId(productId = productDisplay.productId)
                sut.shouldNotBeNull()
                sut.price shouldBe productDisplay.price
            }
        }
    }

    Given("전시 상품 정보가 존재하고 새로운 가격보다 저렴한 경우") {
        val productDisplay = productDisplayRepository.save(makeProductDisplay(price = 30000))

        val command = makeSaveOrUpdateProductDisplayCommand(price = 20000)

        When("저장 또는 업데이트 요청을 하면") {
            saveOrUpdateProductDisplayService.execute(command = command)

            Then("전시 상품 정보를 업데이트한다") {
                val sut = productDisplayRepository.findByProductId(productId = productDisplay.productId)
                sut.shouldNotBeNull()
                sut.price shouldBe command.price
            }
        }
    }
})
