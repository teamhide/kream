package com.teamhide.kream.bidding

import com.teamhide.kream.bidding.domain.model.Bidding
import com.teamhide.kream.bidding.domain.usecase.BidCommand
import com.teamhide.kream.bidding.domain.vo.BiddingStatus
import com.teamhide.kream.bidding.domain.vo.BiddingType
import com.teamhide.kream.product.domain.model.Product
import com.teamhide.kream.product.makeProduct
import com.teamhide.kream.user.domain.model.User
import com.teamhide.kream.user.makeUser

fun makeBidding(
    id: Long = 1L,
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
