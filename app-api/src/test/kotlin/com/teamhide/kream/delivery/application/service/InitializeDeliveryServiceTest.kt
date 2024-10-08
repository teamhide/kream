package com.teamhide.kream.delivery.application.service

import com.teamhide.kream.delivery.domain.repository.DeliveryRepositoryAdapter
import com.teamhide.kream.delivery.domain.usecase.ProductExternalPort
import com.teamhide.kream.delivery.makeDelivery
import com.teamhide.kream.delivery.makeInitializeDeliveryCommand
import com.teamhide.kream.product.makeBidding
import com.teamhide.kream.product.makeProduct
import io.kotest.core.spec.style.BehaviorSpec
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify

internal class InitializeDeliveryServiceTest : BehaviorSpec({
    val deliveryRepositoryAdapter = mockk<DeliveryRepositoryAdapter>()
    val productExternalPort = mockk<ProductExternalPort>()
    val initializeDeliveryService = InitializeDeliveryService(
        deliveryRepositoryAdapter = deliveryRepositoryAdapter,
        productExternalPort = productExternalPort,
    )

    Given("InitializeDeliveryService") {
        When("존재하지 않는 입찰에 대해 배송 초기 설정을 요청하면") {
            val command = makeInitializeDeliveryCommand()
            every { productExternalPort.findBiddingById(any()) } returns null
            initializeDeliveryService.execute(command = command)

            Then("배송 정보를 설정하지 않는다") {
                verify(exactly = 0) { deliveryRepositoryAdapter.save(any()) }
            }
        }

        When("존재하지 않는 상품에 대해 배송 초기 설정을 요청하면") {
            val command = makeInitializeDeliveryCommand()
            val bidding = makeBidding()
            every { productExternalPort.findBiddingById(any()) } returns bidding
            every { productExternalPort.findProductById(any()) } returns null
            initializeDeliveryService.execute(command = command)

            Then("배송 정보를 설정하지 않는다") {
                verify(exactly = 0) { deliveryRepositoryAdapter.save(any()) }
            }
        }

        When("완료된 입찰에 대해 배송 초기 설정을 요청하면") {
            val command = makeInitializeDeliveryCommand()
            val bidding = makeBidding()
            every { productExternalPort.findBiddingById(any()) } returns bidding

            val product = makeProduct()
            every { productExternalPort.findProductById(any()) } returns product

            val delivery = makeDelivery()
            every { deliveryRepositoryAdapter.save(any()) } returns delivery

            initializeDeliveryService.execute(command = command)

            Then("배송 정보를 설정한다") {
                verify(exactly = 1) { deliveryRepositoryAdapter.save(any()) }
            }
        }
    }
})
