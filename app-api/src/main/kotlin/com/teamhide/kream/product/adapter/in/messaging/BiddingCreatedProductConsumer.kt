package com.teamhide.kream.product.adapter.`in`.messaging

import com.teamhide.kream.bidding.domain.event.BiddingCreatedEvent
import com.teamhide.kream.bidding.domain.vo.BiddingType
import com.teamhide.kream.product.domain.usecase.SaveOrUpdateProductDisplayCommand
import com.teamhide.kream.product.domain.usecase.SaveOrUpdateProductDisplayUseCase
import org.springframework.kafka.annotation.KafkaListener
import org.springframework.stereotype.Component

@Component
class BiddingCreatedProductConsumer(
    private val saveOrUpdateProductDisplayUseCase: SaveOrUpdateProductDisplayUseCase,
) {
    @KafkaListener(
        topics = ["\${spring.kafka.topic.bidding-created}"],
        groupId = "hide",
        containerFactory = "kafkaListenerContainerFactory"
    )
    fun listen(message: BiddingCreatedEvent) {
        if (!isValidBiddingType(biddingType = message.biddingType)) {
            return
        }
        val command = message.let {
            SaveOrUpdateProductDisplayCommand(
                productId = it.productId,
                price = it.price,
                biddingId = it.biddingId,
            )
        }
        saveOrUpdateProductDisplayUseCase.execute(command = command)
    }

    private fun isValidBiddingType(biddingType: String): Boolean {
        return try {
            BiddingType.valueOf(biddingType) == BiddingType.SALE
        } catch (e: IllegalArgumentException) {
            return false
        }
    }
}
