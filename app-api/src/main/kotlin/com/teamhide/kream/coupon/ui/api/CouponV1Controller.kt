package com.teamhide.kream.coupon.ui.api

import com.teamhide.kream.common.response.ApiResponse
import com.teamhide.kream.coupon.domain.usecase.GetAllCouponQuery
import com.teamhide.kream.coupon.domain.usecase.GetAllCouponUseCase
import com.teamhide.kream.coupon.domain.usecase.RegisterCouponCommand
import com.teamhide.kream.coupon.domain.usecase.RegisterCouponUseCase
import com.teamhide.kream.coupon.ui.api.dto.GetAllCouponResponse
import com.teamhide.kream.coupon.ui.api.dto.RegisterCouponRequest
import com.teamhide.kream.coupon.ui.api.dto.RegisterCouponResponse
import jakarta.validation.Valid
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/v1/coupon")
class CouponV1Controller(
    private val registerCouponUseCase: RegisterCouponUseCase,
    private val getAllCouponUseCase: GetAllCouponUseCase,
) {
    @GetMapping("")
    fun getAllCoupon(@RequestParam("pageSize") pageSize: Int, @RequestParam("offset") offset: Int): ApiResponse<GetAllCouponResponse> {
        val query = GetAllCouponQuery(pageSize = pageSize, offset = offset)
        val response = GetAllCouponResponse(data = getAllCouponUseCase.execute(query = query))
        return ApiResponse.success(body = response, statusCode = HttpStatus.OK)
    }

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
