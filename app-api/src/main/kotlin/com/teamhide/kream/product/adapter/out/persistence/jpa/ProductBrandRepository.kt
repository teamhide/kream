package com.teamhide.kream.product.adapter.out.persistence.jpa

import com.teamhide.kream.product.domain.model.ProductBrand
import org.springframework.data.jpa.repository.JpaRepository

interface ProductBrandRepository : JpaRepository<ProductBrand, Long>
