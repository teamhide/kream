package com.teamhide.kream.product.domain.model

data class ProductDetail(
    val productId: Long,
    val releasePrice: Int,
    val modelNumber: String,
    val name: String,
    val brand: String,
    val category: String,
    val purchaseBidPrice: Int?,
    val saleBidPrice: Int?,
)
