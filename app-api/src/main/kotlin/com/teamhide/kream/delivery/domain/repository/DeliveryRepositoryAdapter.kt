package com.teamhide.kream.delivery.domain.repository

import com.teamhide.kream.delivery.domain.model.Delivery
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Component

@Component
class DeliveryRepositoryAdapter(
    private val deliveryRepository: DeliveryRepository,
) {
    fun save(delivery: Delivery): Delivery {
        return deliveryRepository.save(delivery)
    }

    fun findById(deliveryId: Long): Delivery? {
        return deliveryRepository.findByIdOrNull(deliveryId)
    }
}
