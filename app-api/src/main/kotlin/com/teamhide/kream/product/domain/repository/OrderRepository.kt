package com.teamhide.kream.product.domain.repository

import com.teamhide.kream.product.domain.model.Order
import org.springframework.data.jpa.repository.JpaRepository

interface OrderRepository : JpaRepository<Order, Long>, OrderQuerydslRepository
