package com.teamhide.kream.product.ui.api.dto

import com.teamhide.kream.product.domain.usecase.RegisterProductCommand
import com.teamhide.kream.product.domain.vo.SizeType
import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.NotNull

data class RegisterProductRequest(
    @field:NotBlank
    val name: String,

    @field:NotNull
    val releasePrice: Int,

    @field:NotBlank
    val modelNumber: String,

    @field:NotNull
    val sizeType: SizeType,

    @field:NotNull
    val brandId: Long,

    @field:NotNull
    val categoryId: Long,
) {
    fun toCommand(): RegisterProductCommand {
        return RegisterProductCommand(
            name = this.name,
            releasePrice = this.releasePrice,
            modelNumber = this.modelNumber,
            sizeType = this.sizeType,
            brandId = this.brandId,
            categoryId = this.categoryId,
        )
    }
}
