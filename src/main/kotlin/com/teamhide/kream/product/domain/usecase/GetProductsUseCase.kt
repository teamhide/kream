package com.teamhide.kream.product.domain.usecase

import com.teamhide.kream.product.domain.model.ProductDisplayRead

data class GetProductsQuery(val page: Int, val size: Int)

interface GetProductsUseCase {
    fun execute(query: GetProductsQuery): List<ProductDisplayRead>
}
