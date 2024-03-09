package com.teamhide.kream.product.domain.repository

import com.teamhide.kream.product.domain.model.SaleHistory
import org.springframework.data.jpa.repository.JpaRepository

interface SaleHistoryRepository : JpaRepository<SaleHistory, Long>, SaleHistoryQuerydslRepository
