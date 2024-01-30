package com.teamhide.kream.product.adapter.out.persistence

import com.teamhide.kream.product.adapter.out.persistence.jpa.ProductBrandRepository
import com.teamhide.kream.product.adapter.out.persistence.jpa.ProductCategoryRepository
import com.teamhide.kream.product.adapter.out.persistence.jpa.ProductRepository
import com.teamhide.kream.product.domain.model.Product
import com.teamhide.kream.product.domain.model.ProductBrand
import com.teamhide.kream.product.domain.model.ProductCategory
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Component

@Component
class ProductRepositoryAdapter(
    private val productRepository: ProductRepository,
    private val productBrandRepository: ProductBrandRepository,
    private val productCategoryRepository: ProductCategoryRepository,
) {
    fun findById(productId: Long): Product? {
        return productRepository.findByIdOrNull(id = productId)
    }

    fun saveProduct(product: Product): Product {
        return productRepository.save(product)
    }

    fun findCategoryById(categoryId: Long): ProductCategory? {
        return productCategoryRepository.findByIdOrNull(categoryId)
    }

    fun findBrandById(brandId: Long): ProductBrand? {
        return productBrandRepository.findByIdOrNull(brandId)
    }
}
