package com.teamhide.kream.delivery

import com.teamhide.kream.delivery.domain.model.Delivery
import com.teamhide.kream.delivery.domain.usecase.InitializeDeliveryCommand
import com.teamhide.kream.delivery.domain.vo.DeliveryStatus
import com.teamhide.kream.product.domain.model.Bidding
import com.teamhide.kream.product.makeBidding

fun makeDelivery(
    id: Long = 1L,
    bidding: Bidding = makeBidding(),
    status: DeliveryStatus = DeliveryStatus.PENDING,
): Delivery {
    return Delivery(id = id, bidding = bidding, status = status)
}

fun makeInitializeDeliveryCommand(
    productId: Long = 1L,
    biddingId: Long = 1L,
): InitializeDeliveryCommand {
    return InitializeDeliveryCommand(
        productId = productId,
        biddingId = biddingId,
    )
}
