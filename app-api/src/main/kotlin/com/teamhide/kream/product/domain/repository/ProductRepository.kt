package com.teamhide.kream.product.domain.repository

import com.teamhide.kream.product.domain.model.Product
import org.springframework.data.jpa.repository.JpaRepository

interface ProductRepository : JpaRepository<Product, Long>, ProductQuerydslRepository
