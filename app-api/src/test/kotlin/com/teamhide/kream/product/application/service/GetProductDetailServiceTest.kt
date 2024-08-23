package com.teamhide.kream.product.application.service

import com.teamhide.kream.product.application.exception.ProductNotFoundException
import com.teamhide.kream.product.domain.repository.BiddingRepository
import com.teamhide.kream.product.domain.repository.ProductBrandRepository
import com.teamhide.kream.product.domain.repository.ProductCategoryRepository
import com.teamhide.kream.product.domain.repository.ProductRepository
import com.teamhide.kream.product.domain.usecase.GetProductDetailQuery
import com.teamhide.kream.product.domain.vo.BiddingType
import com.teamhide.kream.product.makeBidding
import com.teamhide.kream.product.makeProduct
import com.teamhide.kream.product.makeProductBrand
import com.teamhide.kream.product.makeProductCategory
import com.teamhide.kream.support.test.IntegrationTest
import com.teamhide.kream.support.test.MysqlDbCleaner
import io.kotest.assertions.throwables.shouldThrow
import io.kotest.core.spec.style.BehaviorSpec
import io.kotest.matchers.nulls.shouldNotBeNull
import io.kotest.matchers.shouldBe

@IntegrationTest
internal class GetProductDetailServiceTest(
    private val productRepository: ProductRepository,
    private val productBrandRepository: ProductBrandRepository,
    private val productCategoryRepository: ProductCategoryRepository,
    private val biddingRepository: BiddingRepository,
    private val getProductDetailService: GetProductDetailService,
) : BehaviorSpec({
    listeners(MysqlDbCleaner())

    Given("GetProductDetailService") {
        When("존재하지 않는 없는 상품을 대상으로 상세 정보 조회를 요청하면") {
            val query = GetProductDetailQuery(productId = 1L)

            Then("예외가 발생한다") {
                shouldThrow<ProductNotFoundException> { getProductDetailService.execute(query = query) }
            }
        }

        When("입찰 정보가 없는 상품에 대해 상세 정보 조회를 요청하면") {
            val productBrand = productBrandRepository.save(makeProductBrand())

            val productCategory = productCategoryRepository.save(makeProductCategory())

            val product = productRepository.save(makeProduct(productBrand = productBrand, productCategory = productCategory))

            val query = GetProductDetailQuery(productId = product.id)

            val sut = getProductDetailService.execute(query = query)

            Then("입찰 가격을 제외한 정보를 채워서 리턴된다") {
                sut.shouldNotBeNull()
                sut.productId shouldBe product.id
                sut.releasePrice shouldBe product.releasePrice
                sut.modelNumber shouldBe product.modelNumber
                sut.name shouldBe product.name
                sut.brand shouldBe productBrand.name
                sut.category shouldBe productCategory.name
                sut.purchaseBidPrice shouldBe null
                sut.saleBidPrice shouldBe null
            }
        }

        When("존재하는 상품에 대해 상세 정보 조회를 요청하면") {
            val productBrand = productBrandRepository.save(makeProductBrand())

            val productCategory = productCategoryRepository.save(makeProductCategory())

            val product = productRepository.save(makeProduct(productBrand = productBrand, productCategory = productCategory))

            val query = GetProductDetailQuery(productId = product.id)

            val mostCheapestBidding = makeBidding(price = 1000, product = product, biddingType = BiddingType.SALE)
            biddingRepository.save(mostCheapestBidding)

            val mostExpensiveBidding = makeBidding(price = 20000, product = product, biddingType = BiddingType.PURCHASE)
            biddingRepository.save(mostExpensiveBidding)

            val sut = getProductDetailService.execute(query = query)

            Then("모든 정보를 채워서 리턴된다") {
                sut.shouldNotBeNull()
                sut.productId shouldBe product.id
                sut.releasePrice shouldBe product.releasePrice
                sut.modelNumber shouldBe product.modelNumber
                sut.name shouldBe product.name
                sut.brand shouldBe productBrand.name
                sut.category shouldBe productCategory.name
                sut.purchaseBidPrice shouldBe mostCheapestBidding.price
                sut.saleBidPrice shouldBe mostExpensiveBidding.price
            }
        }
    }
})
