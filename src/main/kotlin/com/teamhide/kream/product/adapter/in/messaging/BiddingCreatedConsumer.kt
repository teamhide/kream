package com.teamhide.kream.product.adapter.`in`.messaging

import com.teamhide.kream.bidding.domain.event.BiddingCreatedEvent
import com.teamhide.kream.bidding.domain.vo.BiddingType
import com.teamhide.kream.product.domain.usecase.SaveOrUpdateProductDisplayCommand
import com.teamhide.kream.product.domain.usecase.SaveOrUpdateProductDisplayUseCase
import org.springframework.kafka.annotation.KafkaListener
import org.springframework.stereotype.Component

@Component
class BiddingCreatedConsumer(
    private val saveOrUpdateProductDisplayUseCase: SaveOrUpdateProductDisplayUseCase,
) {
    @KafkaListener(
        topics = ["\${spring.kafka.topic.bidding-created}"],
        groupId = "hide",
        containerFactory = "kafkaListenerContainerFactory"
    )
    fun listen(message: BiddingCreatedEvent) {
        try {
            if (BiddingType.valueOf(message.biddingType) == BiddingType.PURCHASE) {
                return
            }
        } catch (e: IllegalArgumentException) {
            return
        }
        val command = message.let {
            SaveOrUpdateProductDisplayCommand(
                productId = it.productId,
                price = it.price,
            )
        }
        saveOrUpdateProductDisplayUseCase.execute(command = command)
    }
}
