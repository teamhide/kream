package com.teamhide.kream.delivery.application.service

import com.teamhide.kream.delivery.adapter.out.persistence.DeliveryRepositoryAdapter
import com.teamhide.kream.delivery.makeDelivery
import com.teamhide.kream.delivery.makeInitializeDeliveryCommand
import com.teamhide.kream.product.adapter.out.persistence.BiddingRepositoryAdapter
import com.teamhide.kream.product.adapter.out.persistence.ProductRepositoryAdapter
import com.teamhide.kream.product.makeBidding
import com.teamhide.kream.product.makeProduct
import io.kotest.core.spec.style.BehaviorSpec
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify

class InitializeDeliveryServiceTest : BehaviorSpec({
    val deliveryRepositoryAdapter = mockk<DeliveryRepositoryAdapter>()
    val biddingRepositoryAdapter = mockk<BiddingRepositoryAdapter>()
    val productRepositoryAdapter = mockk<ProductRepositoryAdapter>()
    val initializeDeliveryService = InitializeDeliveryService(
        deliveryRepositoryAdapter = deliveryRepositoryAdapter,
        biddingRepositoryAdapter = biddingRepositoryAdapter,
        productRepositoryAdapter = productRepositoryAdapter,
    )

    Given("존재하지 않는 입찰에 대해") {
        val command = makeInitializeDeliveryCommand()
        every { biddingRepositoryAdapter.findById(any()) } returns null

        When("배송 초기 설정을 요청하면") {
            initializeDeliveryService.execute(command = command)

            Then("배송 정보를 설정하지 않는다") {
                verify(exactly = 0) { deliveryRepositoryAdapter.save(any()) }
            }
        }
    }

    Given("존재하지 않는 상품에 대해") {
        val command = makeInitializeDeliveryCommand()
        val bidding = makeBidding()
        every { biddingRepositoryAdapter.findById(any()) } returns bidding
        every { productRepositoryAdapter.findById(any()) } returns null

        When("배송 초기 설정을 요청하면") {
            initializeDeliveryService.execute(command = command)

            Then("배송 정보를 설정하지 않는다") {
                verify(exactly = 0) { deliveryRepositoryAdapter.save(any()) }
            }
        }
    }

    Given("완료된 입찰에 대해") {
        val command = makeInitializeDeliveryCommand()
        val bidding = makeBidding()
        every { biddingRepositoryAdapter.findById(any()) } returns bidding

        val product = makeProduct()
        every { productRepositoryAdapter.findById(any()) } returns product

        val delivery = makeDelivery()
        every { deliveryRepositoryAdapter.save(any()) } returns delivery

        When("배송 초기 설정을 요청하면") {
            initializeDeliveryService.execute(command = command)

            Then("배송 정보를 설정한다") {
                verify(exactly = 1) { deliveryRepositoryAdapter.save(any()) }
            }
        }
    }
})
