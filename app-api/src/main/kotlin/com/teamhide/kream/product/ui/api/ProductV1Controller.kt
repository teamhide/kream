package com.teamhide.kream.product.ui.api

import com.teamhide.kream.common.response.ApiResponse
import com.teamhide.kream.product.domain.model.ProductDisplayRead
import com.teamhide.kream.product.domain.usecase.GetProductsQuery
import com.teamhide.kream.product.domain.usecase.GetProductsUseCase
import com.teamhide.kream.product.domain.usecase.RegisterProductCommand
import com.teamhide.kream.product.domain.usecase.RegisterProductUseCase
import com.teamhide.kream.product.domain.vo.SizeType
import jakarta.validation.Valid
import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.NotNull
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController

data class GetProductsResponse(val data: List<ProductDisplayRead>)

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
)

data class RegisterProductResponse(
    val id: Long,
    val name: String,
    val releasePrice: Int,
    val modelNumber: String,
    val sizeType: SizeType,
    val brand: String,
    val category: String,
)

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
        val command = body.let {
            RegisterProductCommand(
                name = it.name,
                releasePrice = it.releasePrice,
                modelNumber = it.modelNumber,
                sizeType = it.sizeType,
                brandId = it.brandId,
                categoryId = it.categoryId,
            )
        }
        val response = registerProductUseCase.execute(command = command).let {
            RegisterProductResponse(
                id = it.id,
                name = it.name,
                releasePrice = it.releasePrice,
                modelNumber = it.modelNumber,
                sizeType = it.sizeType,
                brand = it.brand,
                category = it.category,
            )
        }
        return ApiResponse.success(body = response, statusCode = HttpStatus.OK)
    }
}
