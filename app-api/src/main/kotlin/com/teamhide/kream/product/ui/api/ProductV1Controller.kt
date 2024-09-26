package com.teamhide.kream.product.ui.api

import com.teamhide.kream.common.response.ApiResponse
import com.teamhide.kream.product.domain.usecase.GetProductsQuery
import com.teamhide.kream.product.domain.usecase.GetProductsUseCase
import com.teamhide.kream.product.domain.usecase.RegisterProductUseCase
import com.teamhide.kream.product.ui.api.dto.GetProductsResponse
import com.teamhide.kream.product.ui.api.dto.RegisterProductRequest
import com.teamhide.kream.product.ui.api.dto.RegisterProductResponse
import jakarta.validation.Valid
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/v1/product")
class ProductV1Controller(
    private val getProductsUseCase: GetProductsUseCase,
    private val registerProductUseCase: RegisterProductUseCase,
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

    @PostMapping("")
    fun registerProduct(@RequestBody @Valid body: RegisterProductRequest): ApiResponse<RegisterProductResponse> {
        val command = body.toCommand()
        val productResponseDto = registerProductUseCase.execute(command = command)
        val response = RegisterProductResponse.from(productDto = productResponseDto)
        return ApiResponse.success(body = response, statusCode = HttpStatus.OK)
    }
}
