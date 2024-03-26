package com.teamhide.kream.coupon.domain.model

import com.teamhide.kream.common.config.database.BaseTimestampEntity
import com.teamhide.kream.coupon.domain.vo.CouponDiscountInfo
import com.teamhide.kream.coupon.domain.vo.CouponDiscountType
import com.teamhide.kream.coupon.domain.vo.CouponGroupStatus
import com.teamhide.kream.coupon.domain.vo.CouponPeriodType
import jakarta.persistence.Column
import jakarta.persistence.Embedded
import jakarta.persistence.Entity
import jakarta.persistence.EnumType
import jakarta.persistence.Enumerated
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.Table

@Entity
@Table(
    name = "coupon_group",
)
class CouponGroup(
    @Column(name = "identifier", nullable = false, length = 32)
    val identifier: String,

    @Embedded
    val discountInfo: CouponDiscountInfo,

    @Column(name = "status", nullable = false, length = 20)
    @Enumerated(EnumType.STRING)
    val status: CouponGroupStatus,

    @Column(name = "quantity", nullable = false)
    var quantity: Int,

    @Column(name = "remain_quantity", nullable = false)
    var remainQuantity: Int,

    @Column(name = "period_type", nullable = false)
    @Enumerated(EnumType.STRING)
    val periodType: CouponPeriodType,

    @Column(name = "period", nullable = false)
    val period: Int,

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long = 0L,
) : BaseTimestampEntity() {
    fun isAvailable(): Boolean {
        return this.status == CouponGroupStatus.ACTIVATED
    }

    fun decreaseRemainQuantity() {
        this.remainQuantity -= 1
    }

    companion object {
        fun create(
            identifier: String,
            discountType: CouponDiscountType,
            discountValue: Int,
            quantity: Int,
            periodType: CouponPeriodType,
            period: Int,
        ): CouponGroup {
            return CouponGroup(
                identifier = identifier,
                discountInfo = CouponDiscountInfo(discountType = discountType, discountValue = discountValue),
                status = CouponGroupStatus.ACTIVATED,
                quantity = quantity,
                remainQuantity = quantity,
                periodType = periodType,
                period = period,
            )
        }
    }
}
