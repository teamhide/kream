package com.teamhide.kream.product.adapter.out.persistence

import com.teamhide.kream.product.adapter.out.persistence.mongo.ProductDisplayRepository
import com.teamhide.kream.product.domain.model.ProductDisplay
import org.springframework.data.domain.PageRequest
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

    fun findAllBy(page: Int, size: Int): List<ProductDisplay> {
        val pageRequest = PageRequest.of(page, size)
        return productDisplayRepository.findAllBy(pageable = pageRequest)
    }
}
