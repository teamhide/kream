package com.teamhide.kream.product.adapter.out.persistence.jpa

import com.querydsl.core.annotations.QueryProjection

data class ProductDetailDto @QueryProjection constructor(
    val productId: Long,
    val releasePrice: Int,
    val modelNumber: String,
    val name: String,
    val brand: String,
    val category: String,
)

interface ProductQuerydslRepository {
    fun findDetailById(productId: Long): ProductDetailDto?
}