package com.teamhide.kream.delivery.adapter.out.persistence

import com.teamhide.kream.delivery.adapter.out.persistence.jpa.DeliveryRepository
import com.teamhide.kream.delivery.makeDelivery
import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.nulls.shouldNotBeNull
import io.kotest.matchers.shouldBe
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.springframework.data.repository.findByIdOrNull

internal class DeliveryRepositoryAdapterTest : StringSpec({
    val deliveryRepository = mockk<DeliveryRepository>()
    val deliveryRepositoryAdapter = DeliveryRepositoryAdapter(deliveryRepository = deliveryRepository)

    "Delivery를 저장한다" {
        // Given
        val delivery = makeDelivery()
        every { deliveryRepository.save(any()) } returns delivery

        // When
        val sut = deliveryRepositoryAdapter.save(delivery = delivery)

        // Then
        sut.id shouldBe delivery.id
        sut.bidding shouldBe delivery.bidding
        sut.status shouldBe delivery.status
        verify(exactly = 1) { deliveryRepository.save(any()) }
    }

    "id로 Delivery를 조회한다" {
        // Given
        val deliveryId = 1L
        val delivery = makeDelivery(id = deliveryId)
        every { deliveryRepository.findByIdOrNull(any()) } returns delivery

        // When
        val sut = deliveryRepositoryAdapter.findById(deliveryId = deliveryId)

        // Then
        sut.shouldNotBeNull()
        sut.id shouldBe delivery.id
        sut.bidding shouldBe delivery.bidding
        sut.status shouldBe delivery.status
        verify(exactly = 1) { deliveryRepository.findByIdOrNull(any()) }
    }
})
