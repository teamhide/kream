package com.teamhide.kream.product.domain.repository

import com.querydsl.core.annotations.QueryProjection

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
}
