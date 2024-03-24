package com.teamhide.kream.product.domain.repository

import com.querydsl.core.annotations.QueryProjection
import com.teamhide.kream.product.domain.model.Product

data class ProductInfoDto @QueryProjection constructor(
    val productId: Long,
    val releasePrice: Int,
    val modelNumber: String,
    val name: String,
    val brand: String,
    val category: String,
)

interface ProductQuerydslRepository {
    fun findInfoById(productId: Long): ProductInfoDto?

    fun findWithCategoryAndBrandById(productId: Long): Product?
}
