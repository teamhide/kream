package com.teamhide.kream.coupon.domain.repository

import com.teamhide.kream.coupon.domain.model.CouponHistory
import org.springframework.data.jpa.repository.JpaRepository

interface CouponHistoryRepository : JpaRepository<CouponHistory, Long>
