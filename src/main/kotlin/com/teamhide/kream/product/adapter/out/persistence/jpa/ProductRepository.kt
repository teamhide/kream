package com.teamhide.kream.product.adapter.out.persistence.jpa

import com.teamhide.kream.product.domain.model.Product
import org.springframework.data.jpa.repository.JpaRepository

interface ProductRepository : JpaRepository<Product, Long>
