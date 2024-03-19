package com.teamhide.kream.coupon.domain.model

import com.teamhide.kream.common.config.database.BaseTimestampEntity
import com.teamhide.kream.coupon.domain.vo.CouponPeriodType
import com.teamhide.kream.coupon.domain.vo.CouponStatus
import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.EnumType
import jakarta.persistence.Enumerated
import jakarta.persistence.FetchType
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.JoinColumn
import jakarta.persistence.ManyToOne
import jakarta.persistence.Table
import java.time.LocalDateTime

@Entity
@Table(name = "coupon")
class Coupon(
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "coupon_group_id")
    val couponGroup: CouponGroup,

    @Column(name = "user_id", nullable = false)
    val userId: Long,

    @Column(name = "status", nullable = false, length = 20)
    @Enumerated(EnumType.STRING)
    val status: CouponStatus,

    @Column(name = "expired_at", nullable = false)
    val expiredAt: LocalDateTime,

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long = 0L,
) : BaseTimestampEntity() {
    companion object {
        fun issue(couponGroup: CouponGroup, userId: Long): Coupon {
            val now = LocalDateTime.now()
            val period = couponGroup.period
            val expiredAt = when (couponGroup.periodType) {
                CouponPeriodType.DAY -> now.plusDays(period.toLong())
                CouponPeriodType.MONTH -> now.plusMonths(period.toLong())
            }
            return Coupon(
                couponGroup = couponGroup,
                userId = userId,
                status = CouponStatus.ISSUED,
                expiredAt = expiredAt,
            )
        }
    }
}
