package com.teamhide.kream.product

import com.teamhide.kream.product.adapter.`in`.api.v1.ImmediatePurchaseRequest
import com.teamhide.kream.product.adapter.`in`.api.v1.ImmediateSaleRequest
import com.teamhide.kream.product.domain.model.Bidding
import com.teamhide.kream.product.domain.model.Order
import com.teamhide.kream.product.domain.model.Product
import com.teamhide.kream.product.domain.model.SaleHistory
import com.teamhide.kream.product.domain.usecase.BidCommand
import com.teamhide.kream.product.domain.usecase.CompleteBidCommand
import com.teamhide.kream.product.domain.usecase.ImmediatePurchaseCommand
import com.teamhide.kream.product.domain.usecase.ImmediateSaleCommand
import com.teamhide.kream.product.domain.vo.BiddingStatus
import com.teamhide.kream.product.domain.vo.BiddingType
import com.teamhide.kream.product.domain.vo.OrderStatus
import com.teamhide.kream.user.domain.model.User
import com.teamhide.kream.user.makeUser

fun makeBidding(
    id: Long = 0L,
    product: Product = makeProduct(),
    user: User = makeUser(),
    price: Int = 20000,
    size: String = "M",
    status: BiddingStatus = BiddingStatus.IN_PROGRESS,
    biddingType: BiddingType = BiddingType.SALE,
): Bidding {
    return Bidding(
        id = id,
        product = product,
        user = user,
        price = price,
        size = size,
        status = status,
        biddingType = biddingType,
    )
}

fun makeBidCommand(
    productId: Long = 1L,
    price: Int = 1000,
    size: String = "M",
    biddingType: BiddingType = BiddingType.PURCHASE,
    userId: Long = 1L,
): BidCommand {
    return BidCommand(
        productId = productId,
        price = price,
        size = size,
        biddingType = biddingType,
        userId = userId,
    )
}

fun makeOrder(
    id: Long = 1L,
    paymentId: String = "paymentId",
    bidding: Bidding = makeBidding(),
    user: User = makeUser(),
    status: OrderStatus = OrderStatus.COMPLETE,
): Order {
    return Order(
        id = id,
        paymentId = paymentId,
        bidding = bidding,
        user = user,
        status = status,
    )
}

fun makeCompleteBidCommand(
    paymentId: String = "paymentId",
    biddingId: Long = 1L,
    userId: Long = 1L,
): CompleteBidCommand {
    return CompleteBidCommand(
        paymentId = paymentId,
        biddingId = biddingId,
        userId = userId,
    )
}

fun makeSaleHistory(
    bidding: Bidding = makeBidding(),
    user: User = makeUser(),
    price: Int = 20000,
    size: String = "M"
): SaleHistory {
    return SaleHistory(
        bidding = bidding,
        user = user,
        price = price,
        size = size,
    )
}

fun makeImmediatePurchaseCommand(
    biddingId: Long = 1L,
    userId: Long = 1L,
): ImmediatePurchaseCommand {
    return ImmediatePurchaseCommand(
        biddingId = biddingId,
        userId = userId,
    )
}

fun makeImmediatePurchaseRequest(biddingId: Long = 1L): ImmediatePurchaseRequest {
    return ImmediatePurchaseRequest(biddingId = biddingId)
}

fun makeImmediateSaleCommand(
    biddingId: Long = 1L,
    userId: Long = 1L,
): ImmediateSaleCommand {
    return ImmediateSaleCommand(
        biddingId = biddingId,
        userId = userId,
    )
}

fun makeImmediateSaleRequest(biddingId: Long = 1L): ImmediateSaleRequest {
    return ImmediateSaleRequest(biddingId = biddingId)
}
