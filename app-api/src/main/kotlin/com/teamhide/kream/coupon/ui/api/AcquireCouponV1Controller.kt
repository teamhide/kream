package com.teamhide.kream.coupon.ui.api

import com.teamhide.kream.common.response.ApiResponse
import com.teamhide.kream.common.security.CurrentUser
import com.teamhide.kream.coupon.domain.usecase.AcquireCouponCommand
import com.teamhide.kream.coupon.domain.usecase.AcquireCouponUseCase
import org.springframework.http.HttpStatus
import org.springframework.security.core.annotation.AuthenticationPrincipal
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/v1/coupon/acquire")
class AcquireCouponV1Controller(
    private val acquireCouponUseCase: AcquireCouponUseCase,
) {
    @PostMapping("/{identifier}")
    fun acquireCoupon(
        @AuthenticationPrincipal currentUser: CurrentUser,
        @PathVariable("identifier") identifier: String
    ): ApiResponse<Void> {
        val command = AcquireCouponCommand(userId = currentUser.id, identifier = identifier)
        acquireCouponUseCase.execute(command = command)
        return ApiResponse.success(statusCode = HttpStatus.OK)
    }
}
