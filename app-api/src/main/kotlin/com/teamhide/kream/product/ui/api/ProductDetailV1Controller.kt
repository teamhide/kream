package com.teamhide.kream.product.ui.api

import com.teamhide.kream.common.response.ApiResponse
import com.teamhide.kream.product.domain.usecase.GetProductDetailQuery
import com.teamhide.kream.product.domain.usecase.GetProductDetailUseCase
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

data class GetProductResponse(
    val productId: Long,
    val releasePrice: Int,
    val modelNumber: String,
    val name: String,
    val brand: String,
    val category: String,
    val purchaseBidPrice: Int?,
    val saleBidPrice: Int?,
)

@RestController
@RequestMapping("/v1/product")
class ProductDetailV1Controller(
    private val getProductDetailUseCase: GetProductDetailUseCase,
) {
    @GetMapping("/{productId}")
    fun getProductDetail(@PathVariable("productId") productId: Long): ApiResponse<GetProductResponse> {
        val query = GetProductDetailQuery(productId = productId)
        val response = getProductDetailUseCase.execute(query = query).let {
            GetProductResponse(
                productId = it.productId,
                releasePrice = it.releasePrice,
                modelNumber = it.modelNumber,
                name = it.name,
                brand = it.brand,
                category = it.category,
                purchaseBidPrice = it.purchaseBidPrice,
                saleBidPrice = it.saleBidPrice,
            )
        }
        return ApiResponse.success(body = response, statusCode = HttpStatus.OK)
    }
}
