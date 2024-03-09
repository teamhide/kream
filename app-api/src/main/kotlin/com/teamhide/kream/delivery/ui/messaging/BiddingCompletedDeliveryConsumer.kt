package com.teamhide.kream.delivery.ui.messaging

import com.teamhide.kream.delivery.domain.usecase.InitializeDeliveryCommand
import com.teamhide.kream.delivery.domain.usecase.InitializeDeliveryUseCase
import com.teamhide.kream.product.domain.event.BiddingCompletedEvent
import org.springframework.kafka.annotation.KafkaListener
import org.springframework.stereotype.Component

@Component
class BiddingCompletedDeliveryConsumer(
    private val initializedDeliveryUseCase: InitializeDeliveryUseCase,
) {
    @KafkaListener(
        topics = ["\${spring.kafka.topic.bidding-completed}"],
        groupId = "hide",
        containerFactory = "kafkaListenerContainerFactory"
    )
    fun listen(message: BiddingCompletedEvent) {
        val command = message.let {
            InitializeDeliveryCommand(
                productId = it.productId,
                biddingId = it.biddingId,
            )
        }
        initializedDeliveryUseCase.execute(command = command)
    }
}
