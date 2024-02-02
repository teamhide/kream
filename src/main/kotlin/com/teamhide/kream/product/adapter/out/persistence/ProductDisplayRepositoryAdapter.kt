package com.teamhide.kream.product.adapter.out.persistence

import com.teamhide.kream.product.adapter.out.persistence.mongo.ProductDisplayRepository
import org.springframework.stereotype.Component

@Component
class ProductDisplayRepositoryAdapter(
    private val productDisplayRepository: ProductDisplayRepository,
)
