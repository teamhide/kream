package com.teamhide.kream.product.ui.api

import com.teamhide.kream.common.response.ApiResponse
import com.teamhide.kream.product.domain.usecase.GetProductDetailQuery
import com.teamhide.kream.product.domain.usecase.GetProductDetailUseCase
import com.teamhide.kream.product.ui.api.dto.GetProductResponse
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/v1/product")
class ProductDetailV1Controller(
    private val getProductDetailUseCase: GetProductDetailUseCase,
) {
    @GetMapping("/{productId}")
    fun getProductDetail(@PathVariable("productId") productId: Long): ApiResponse<GetProductResponse> {
        val query = GetProductDetailQuery(productId = productId)
        val productDetail = getProductDetailUseCase.execute(query = query)
        val response = GetProductResponse.from(productDetail = productDetail)
        return ApiResponse.success(body = response, statusCode = HttpStatus.OK)
    }
}
