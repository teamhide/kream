package com.teamhide.kream.coupon.ui.api

import com.teamhide.kream.common.response.ApiResponse
import com.teamhide.kream.coupon.domain.usecase.RegisterCouponCommand
import com.teamhide.kream.coupon.domain.usecase.RegisterCouponUseCase
import com.teamhide.kream.coupon.domain.vo.CouponDiscountType
import com.teamhide.kream.coupon.domain.vo.CouponPeriodType
import jakarta.validation.Valid
import org.jetbrains.annotations.NotNull
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

data class RegisterCouponRequest(
    @field:NotNull
    val discountType: CouponDiscountType,

    @field:NotNull
    val discountValue: Int,

    @field:NotNull
    val quantity: Int,

    @field:NotNull
    val periodType: CouponPeriodType,

    @field:NotNull
    val period: Int,
)

data class RegisterCouponResponse(val identifier: String, val quantity: Int)

@RestController
@RequestMapping("/v1/coupon")
class CouponV1Controller(
    private val registerCouponUseCase: RegisterCouponUseCase,
) {
    @PostMapping("")
    fun registerCoupon(@RequestBody @Valid body: RegisterCouponRequest): ApiResponse<RegisterCouponResponse> {
        val command = body.let {
            RegisterCouponCommand(
                discountType = it.discountType,
                discountValue = it.discountValue,
                quantity = it.quantity,
                periodType = it.periodType,
                period = it.period,
            )
        }
        val response = registerCouponUseCase.execute(command = command).let {
            RegisterCouponResponse(
                identifier = it.identifier,
                quantity = it.quantity,
            )
        }
        return ApiResponse.success(body = response, statusCode = HttpStatus.OK)
    }
}
