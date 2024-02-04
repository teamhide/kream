package com.teamhide.kream.product.adapter.out.persistence.mongo

import com.teamhide.kream.product.domain.model.ProductDisplay
import org.springframework.data.mongodb.repository.MongoRepository
import org.springframework.stereotype.Repository

@Repository
interface ProductDisplayRepository : MongoRepository<ProductDisplay, String> {
    fun findByProductId(productId: Long): ProductDisplay?
}
