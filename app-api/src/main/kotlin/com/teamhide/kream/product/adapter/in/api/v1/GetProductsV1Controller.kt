package com.teamhide.kream.product.adapter.`in`.api.v1

import com.teamhide.kream.common.response.ApiResponse
import com.teamhide.kream.product.domain.model.ProductDisplayRead
import com.teamhide.kream.product.domain.usecase.GetProductsQuery
import com.teamhide.kream.product.domain.usecase.GetProductsUseCase
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController

data class GetProductsResponse(val data: List<ProductDisplayRead>)

@RestController
@RequestMapping("/v1/product")
class GetProductsV1Controller(
    private val getProductsUseCase: GetProductsUseCase,
) {
    @GetMapping("")
    fun getProducts(
        @RequestParam("page", defaultValue = "0") page: Int,
        @RequestParam("size", defaultValue = "20") size: Int,
    ): ApiResponse<GetProductsResponse> {
        val query = GetProductsQuery(page = page, size = size)
        val products = getProductsUseCase.execute(query = query)
        return ApiResponse.success(body = GetProductsResponse(data = products), statusCode = HttpStatus.OK)
    }
}
