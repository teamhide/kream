package com.teamhide.kream.coupon.domain.model

import com.teamhide.kream.common.config.database.BaseTimestampEntity
import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.EnumType
import jakarta.persistence.Enumerated
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.JoinColumn
import jakarta.persistence.ManyToOne
import jakarta.persistence.Table

@Entity
@Table(name = "coupon_condition")
class CouponCondition(
    @ManyToOne
    @JoinColumn(name = "coupon_group_id", nullable = false)
    val couponGroup: CouponGroup,

    @Enumerated(EnumType.STRING)
    @Column(name = "condition_type", length = 50, nullable = false)
    var conditionType: ConditionType,

    @Column(name = "condition_value", length = 30, nullable = false)
    val conditionValue: String,

    @Enumerated(EnumType.STRING)
    @Column(name = "condition_value_type", length = 10, nullable = false)
    val conditionValueType: ConditionValueType,

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long = 0L,
) : BaseTimestampEntity() {
    init {
        validateValueType()
    }

    companion object {
    }

    fun toListOfInt(value: String): List<Long> {
        if (!value.startsWith("[") || !value.endsWith("]")) throw InvalidConditionTypeException()

        return value.removeSurrounding("[", "]")
            .split(",")
            .mapNotNull { it.trim().toLongOrNull() }
            .also { if (it.isEmpty()) throw InvalidConditionTypeException() }
    }

    final inline fun <reified T : Any> getTypedValue(): T {
        return when (this.conditionValueType) {
            ConditionValueType.BOOLEAN -> this.conditionValue.toBooleanStrictOrNull() as? T
            ConditionValueType.LIST_LONG -> toListOfInt(value = this.conditionValue) as? T
        } ?: throw InvalidConditionTypeException()
    }

    private fun validateValueType() {
        when (conditionType.valueType) {
            ConditionValueType.BOOLEAN -> getTypedValue<Boolean>()
            ConditionValueType.LIST_LONG -> getTypedValue<List<Int>>()
        }
    }
}

enum class ConditionValueType {
    BOOLEAN,
    LIST_LONG,
}

enum class ConditionType(val valueType: ConditionValueType, val description: String) {
    FIRST_DOWNLOAD(ConditionValueType.BOOLEAN, "쿠폰을 처음 다운로드 받은 유저"),
    ONLY_FOR_SPECIFIC_USERS(ConditionValueType.LIST_LONG, "특정 유저 목록"),
}
