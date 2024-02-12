package com.teamhide.kream.product.domain.usecase

interface PgExternalPort {
    fun attemptPayment(biddingId: Long, price: Int, userId: Long): Result<String>
}
