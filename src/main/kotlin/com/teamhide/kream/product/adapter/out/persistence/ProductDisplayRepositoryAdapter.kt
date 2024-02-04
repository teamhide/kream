package com.teamhide.kream.product.adapter.out.persistence

import com.teamhide.kream.product.adapter.out.persistence.mongo.ProductDisplayRepository
import com.teamhide.kream.product.domain.model.ProductDisplay
import org.springframework.stereotype.Component

@Component
class ProductDisplayRepositoryAdapter(
    private val productDisplayRepository: ProductDisplayRepository,
) {
    fun findByProductId(productId: Long): ProductDisplay? {
        return productDisplayRepository.findByProductId(productId = productId)
    }

    fun save(productDisplay: ProductDisplay): ProductDisplay {
        return productDisplayRepository.save(productDisplay)
    }

    fun findAll(): List<ProductDisplay> {
        return productDisplayRepository.findAll()
    }
}
