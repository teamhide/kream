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
    @JoinColumn(name = "coupon_id", nullable = false)
    val coupon: Coupon,

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
        inline fun <reified T> getTypedValue(conditionValueType: ConditionValueType, conditionValue: String): T {
            return when (conditionValueType) {
                ConditionValueType.BOOLEAN -> conditionValue.toBooleanStrictOrNull() as? T
                ConditionValueType.LIST_INT -> conditionValue.toListOfInt() as? T
            } ?: throw InvalidConditionTypeException()
        }

        fun String.toListOfInt(): List<Int> {
            if (!this.startsWith("[") || !this.endsWith("]")) throw InvalidConditionTypeException()

            return this.substring(1, this.length - 1)
                .split(",")
                .mapNotNull { it.trim().toIntOrNull() }
                .also { if (it.isEmpty()) throw InvalidConditionTypeException() }
        }
    }

    private fun validateValueType() {
        when (conditionType.valueType) {
            ConditionValueType.BOOLEAN -> getTypedValue<Boolean>(conditionValueType, conditionValue)
            ConditionValueType.LIST_INT -> getTypedValue<List<Int>>(conditionValueType, conditionValue)
        }
    }
}

enum class ConditionValueType {
    BOOLEAN,
    LIST_INT,
}

enum class ConditionType(val valueType: ConditionValueType, val description: String) {
    FIRST_DOWNLOAD(ConditionValueType.BOOLEAN, "쿠폰을 처음 다운로드 받은 유저"),
    ONLY_FOR_SPECIFIC_USERS(ConditionValueType.LIST_INT, "특정 유저 목록"),
}
