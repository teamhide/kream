package com.teamhide.kream.product.ui.api.dto

import com.teamhide.kream.product.domain.model.ProductDetail

data class GetProductResponse(
    val productId: Long,
    val releasePrice: Int,
    val modelNumber: String,
    val name: String,
    val brand: String,
    val category: String,
    val purchaseBidPrice: Int?,
    val saleBidPrice: Int?,
) {
    companion object {
        fun from(productDetail: ProductDetail): GetProductResponse {
            return GetProductResponse(
                productId = productDetail.productId,
                releasePrice = productDetail.releasePrice,
                modelNumber = productDetail.modelNumber,
                name = productDetail.name,
                brand = productDetail.brand,
                category = productDetail.category,
                purchaseBidPrice = productDetail.purchaseBidPrice,
                saleBidPrice = productDetail.saleBidPrice,
            )
        }
    }
}
