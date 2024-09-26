package com.teamhide.kream.product.ui.api.dto

import com.teamhide.kream.product.domain.usecase.RegisterProductResponseDto
import com.teamhide.kream.product.domain.vo.SizeType

data class RegisterProductResponse(
    val id: Long,
    val name: String,
    val releasePrice: Int,
    val modelNumber: String,
    val sizeType: SizeType,
    val brand: String,
    val category: String,
) {
    companion object {
        fun from(productDto: RegisterProductResponseDto): RegisterProductResponse {
            return RegisterProductResponse(
                id = productDto.id,
                name = productDto.name,
                releasePrice = productDto.releasePrice,
                modelNumber = productDto.modelNumber,
                sizeType = productDto.sizeType,
                brand = productDto.brand,
                category = productDto.category,
            )
        }
    }
}
