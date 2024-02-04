package com.teamhide.kream.product.domain.model

data class ProductDisplayRead(
    val productId: Long,
    val name: String,
    val price: Int,
    val brand: String,
    val category: String,
)
