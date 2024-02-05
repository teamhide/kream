package com.teamhide.kream.product.domain.model

data class ProductInfo(
    val productId: Long,
    val releasePrice: Int,
    val modelNumber: String,
    val name: String,
    val brand: String,
    val category: String,
)
