package com.teamhide.kream.product.ui.api.dto

import com.teamhide.kream.product.domain.vo.SizeType

data class RegisterProductResponse(
    val id: Long,
    val name: String,
    val releasePrice: Int,
    val modelNumber: String,
    val sizeType: SizeType,
    val brand: String,
    val category: String,
)
