package com.teamhide.kream.product.adapter.out.persistence.mongo

import com.teamhide.kream.product.domain.model.ProductDisplay
import org.springframework.data.domain.Pageable
import org.springframework.data.mongodb.repository.MongoRepository

interface ProductDisplayRepository : MongoRepository<ProductDisplay, String> {
    fun findByProductId(productId: Long): ProductDisplay?

    fun findAllBy(pageable: Pageable): List<ProductDisplay>
}
