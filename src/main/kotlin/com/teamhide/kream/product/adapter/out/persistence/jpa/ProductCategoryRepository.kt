package com.teamhide.kream.product.adapter.out.persistence.jpa

import com.teamhide.kream.product.domain.model.ProductCategory
import org.springframework.data.jpa.repository.JpaRepository

interface ProductCategoryRepository : JpaRepository<ProductCategory, Long>
