package com.teamhide.kream.bidding.adapter.out.persistence.jpa

import com.teamhide.kream.bidding.domain.model.SaleHistory
import org.springframework.data.jpa.repository.JpaRepository

interface SaleHistoryRepository : JpaRepository<SaleHistory, Long>, SaleHistoryQuerydslRepository
