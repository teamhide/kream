package com.teamhide.kream.product.application.service

import com.teamhide.kream.product.application.exception.ProductBrandNotFoundException
import com.teamhide.kream.product.application.exception.ProductCategoryNotFoundException
import com.teamhide.kream.product.domain.model.Product
import com.teamhide.kream.product.domain.model.ProductDisplay
import com.teamhide.kream.product.domain.repository.ProductDisplayRepositoryAdapter
import com.teamhide.kream.product.domain.repository.ProductRepositoryAdapter
import com.teamhide.kream.product.domain.usecase.RegisterProductCommand
import com.teamhide.kream.product.domain.usecase.RegisterProductResponseDto
import com.teamhide.kream.product.domain.usecase.RegisterProductUseCase
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
@Transactional
class RegisterProductService(
    val productRepositoryAdapter: ProductRepositoryAdapter,
    val productDisplayRepositoryAdapter: ProductDisplayRepositoryAdapter,
) : RegisterProductUseCase {
    override fun execute(command: RegisterProductCommand): RegisterProductResponseDto {
        val productBrand =
            productRepositoryAdapter.findBrandById(command.brandId) ?: throw ProductBrandNotFoundException()
        val productCategory =
            productRepositoryAdapter.findCategoryById(command.categoryId) ?: throw ProductCategoryNotFoundException()

        val product = Product(
            name = command.name,
            releasePrice = command.releasePrice,
            modelNumber = command.modelNumber,
            sizeType = command.sizeType,
            productBrand = productBrand,
            productCategory = productCategory,
        )
        val savedProduct = productRepositoryAdapter.saveProduct(product = product).let {
            RegisterProductResponseDto(
                id = it.id,
                name = it.name,
                releasePrice = it.releasePrice,
                modelNumber = it.modelNumber,
                sizeType = it.sizeType,
                brand = productBrand.name,
                category = productCategory.name,
            )
        }
        val productDisplay = ProductDisplay(
            productId = product.id,
            name = product.name,
            price = 0,
            brand = productBrand.name,
            category = productCategory.name,
            lastBiddingId = null,
        )
        productDisplayRepositoryAdapter.save(productDisplay = productDisplay)
        return savedProduct
    }
}
