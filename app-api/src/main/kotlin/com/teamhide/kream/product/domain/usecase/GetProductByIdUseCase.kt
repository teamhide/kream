package com.teamhide.kream.product.domain.usecase

import com.teamhide.kream.product.domain.model.Product

data class GetProductByIdQuery(val productId: Long)

interface GetProductByIdUseCase {
    fun execute(query: GetProductByIdQuery): Product?
}
