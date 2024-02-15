package com.teamhide.kream.product.application.service

import com.teamhide.kream.product.adapter.out.persistence.BiddingRepositoryAdapter
import com.teamhide.kream.product.adapter.out.persistence.ProductRepositoryAdapter
import com.teamhide.kream.product.application.exception.ProductNotFoundException
import com.teamhide.kream.product.domain.usecase.GetProductDetailQuery
import com.teamhide.kream.product.makeBidding
import com.teamhide.kream.product.makeProductInfo
import io.kotest.assertions.throwables.shouldThrow
import io.kotest.core.spec.style.BehaviorSpec
import io.kotest.matchers.nulls.shouldNotBeNull
import io.kotest.matchers.shouldBe
import io.mockk.every
import io.mockk.mockk

internal class GetProductDetailServiceTest : BehaviorSpec({
    val productRepositoryAdapter = mockk<ProductRepositoryAdapter>()
    val biddingRepositoryAdapter = mockk<BiddingRepositoryAdapter>()
    val getProductDetailService = GetProductDetailService(
        productRepositoryAdapter = productRepositoryAdapter,
        biddingRepositoryAdapter = biddingRepositoryAdapter,
    )

    Given("없는 상품을 대상으로") {
        val query = GetProductDetailQuery(productId = 1L)
        every { productRepositoryAdapter.findInfoById(any()) } returns null

        When("상세 정보 조회를 요청하면") {
            Then("예외가 발생한다") {
                shouldThrow<ProductNotFoundException> { getProductDetailService.execute(query = query) }
            }
        }
    }

    Given("입찰 정보가 없는 상품에 대해") {
        val query = GetProductDetailQuery(productId = 1L)
        val productInfo = makeProductInfo()
        every { productRepositoryAdapter.findInfoById(any()) } returns productInfo

        every { biddingRepositoryAdapter.findMostExpensiveBidding(any(), any()) } returns null

        every { biddingRepositoryAdapter.findMostCheapestBidding(any(), any()) } returns null

        When("상세 정보 조회를 요청하면") {
            val sut = getProductDetailService.execute(query = query)

            Then("입찰 가격을 제외한 정보를 채워서 리턴된다") {
                sut.shouldNotBeNull()
                sut.productId shouldBe productInfo.productId
                sut.releasePrice shouldBe productInfo.releasePrice
                sut.modelNumber shouldBe productInfo.modelNumber
                sut.name shouldBe productInfo.name
                sut.brand shouldBe productInfo.brand
                sut.category shouldBe productInfo.category
                sut.purchaseBidPrice shouldBe null
                sut.saleBidPrice shouldBe null
            }
        }
    }

    Given("상품에 대해") {
        val query = GetProductDetailQuery(productId = 1L)
        val productInfo = makeProductInfo()
        every { productRepositoryAdapter.findInfoById(any()) } returns productInfo

        val mostExpensiveBidding = makeBidding(price = 20000)
        every { biddingRepositoryAdapter.findMostExpensiveBidding(any(), any()) } returns mostExpensiveBidding

        val mostCheapestBidding = makeBidding(price = 1000)
        every { biddingRepositoryAdapter.findMostCheapestBidding(any(), any()) } returns mostCheapestBidding

        When("상세 정보 조회를 요청하면") {
            val sut = getProductDetailService.execute(query = query)

            Then("모든 정보를 채워서 리턴된다") {
                sut.shouldNotBeNull()
                sut.productId shouldBe productInfo.productId
                sut.releasePrice shouldBe productInfo.releasePrice
                sut.modelNumber shouldBe productInfo.modelNumber
                sut.name shouldBe productInfo.name
                sut.brand shouldBe productInfo.brand
                sut.category shouldBe productInfo.category
                sut.purchaseBidPrice shouldBe mostCheapestBidding.price
                sut.saleBidPrice shouldBe mostExpensiveBidding.price
            }
        }
    }
})
