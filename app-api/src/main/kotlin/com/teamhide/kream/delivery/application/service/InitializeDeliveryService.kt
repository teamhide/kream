package com.teamhide.kream.delivery.application.service

import com.teamhide.kream.bidding.adapter.out.persistence.BiddingRepositoryAdapter
import com.teamhide.kream.delivery.adapter.out.persistence.DeliveryRepositoryAdapter
import com.teamhide.kream.delivery.domain.model.Delivery
import com.teamhide.kream.delivery.domain.usecase.InitializeDeliveryCommand
import com.teamhide.kream.delivery.domain.usecase.InitializeDeliveryUseCase
import com.teamhide.kream.product.adapter.out.persistence.ProductRepositoryAdapter
import io.github.oshai.kotlinlogging.KotlinLogging
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

private val logger = KotlinLogging.logger { }

@Service
@Transactional
class InitializeDeliveryService(
    private val deliveryRepositoryAdapter: DeliveryRepositoryAdapter,
    private val biddingRepositoryAdapter: BiddingRepositoryAdapter,
    private val productRepositoryAdapter: ProductRepositoryAdapter,
) : InitializeDeliveryUseCase {
    override fun execute(command: InitializeDeliveryCommand) {
        val bidding = biddingRepositoryAdapter.findById(biddingId = command.biddingId) ?: run {
            logger.warn { "InitializeDeliveryService | BiddingNotFound. $command" }
            return
        }

        productRepositoryAdapter.findById(productId = command.productId) ?: run {
            logger.warn { "InitializeDeliveryService | ProductNotFound. $command" }
            return
        }

        val delivery = Delivery.start(bidding = bidding)
        deliveryRepositoryAdapter.save(delivery = delivery)
    }
}
