package com.teamhide.kream.product.application.service

import com.teamhide.kream.product.domain.repository.ProductDisplayRepository
import com.teamhide.kream.product.domain.usecase.GetProductsQuery
import com.teamhide.kream.product.makeProductDisplay
import com.teamhide.kream.support.test.IntegrationTest
import com.teamhide.kream.support.test.MongoDbCleaner
import com.teamhide.kream.support.test.MysqlDbCleaner
import io.kotest.core.spec.style.BehaviorSpec
import io.kotest.matchers.shouldBe

@IntegrationTest
internal class GetProductsServiceTest(
    private val productDisplayRepository: ProductDisplayRepository,
    private val getProductsService: GetProductsService,
) : BehaviorSpec({
    listeners(MysqlDbCleaner(), MongoDbCleaner())

    Given("GetProductsService") {
        When("page와 size를 통해 상품 전시 목록을 요청하면") {
            val query = GetProductsQuery(page = 0, size = 20)
            val product1 = makeProductDisplay(
                productId = 1L,
                name = "name1",
                price = 20000,
                brand = "NIKE",
                category = "SHOES",
                lastBiddingId = 1L,
            )
            val product2 = makeProductDisplay(
                productId = 2L,
                name = "name2",
                price = 30000,
                brand = "MONCLER",
                category = "CLOTHES",
                lastBiddingId = 2L,
            )
            productDisplayRepository.saveAll(listOf(product1, product2))

            val sut = getProductsService.execute(query = query)

            Then("리스트로 넘어온다") {
                sut.size shouldBe 2

                sut[0].productId shouldBe product1.productId
                sut[0].name shouldBe product1.name
                sut[0].price shouldBe product1.price
                sut[0].brand shouldBe product1.brand
                sut[0].category shouldBe product1.category

                sut[1].productId shouldBe product2.productId
                sut[1].name shouldBe product2.name
                sut[1].price shouldBe product2.price
                sut[1].brand shouldBe product2.brand
                sut[1].category shouldBe product2.category
            }
        }
    }
})
