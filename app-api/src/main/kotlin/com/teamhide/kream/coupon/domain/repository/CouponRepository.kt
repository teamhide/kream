package com.teamhide.kream.coupon.domain.repository

import com.teamhide.kream.coupon.domain.model.Coupon
import org.springframework.data.jpa.repository.JpaRepository

interface CouponRepository : JpaRepository<Coupon, Long>
