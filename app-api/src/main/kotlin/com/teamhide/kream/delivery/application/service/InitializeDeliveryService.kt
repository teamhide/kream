package com.teamhide.kream.delivery.application.service

import com.teamhide.kream.delivery.adapter.out.persistence.DeliveryRepositoryAdapter
import com.teamhide.kream.delivery.domain.model.Delivery
import com.teamhide.kream.delivery.domain.usecase.InitializeDeliveryCommand
import com.teamhide.kream.delivery.domain.usecase.InitializeDeliveryUseCase
import com.teamhide.kream.delivery.domain.usecase.ProductExternalPort
import io.github.oshai.kotlinlogging.KotlinLogging
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

private val logger = KotlinLogging.logger { }

@Service
@Transactional
class InitializeDeliveryService(
    private val deliveryRepositoryAdapter: DeliveryRepositoryAdapter,
    private val productExternalPort: ProductExternalPort,
) : InitializeDeliveryUseCase {
    override fun execute(command: InitializeDeliveryCommand) {
        val bidding = productExternalPort.findBiddingById(biddingId = command.biddingId) ?: run {
            logger.warn { "InitializeDeliveryService | BiddingNotFound. $command" }
            return
        }

        productExternalPort.findProductById(productId = command.productId) ?: run {
            logger.warn { "InitializeDeliveryService | ProductNotFound. $command" }
            return
        }

        val delivery = Delivery.start(bidding = bidding)
        deliveryRepositoryAdapter.save(delivery = delivery)
    }
}
