package com.teamhide.kream.product.ui.api

import com.teamhide.kream.common.response.ApiResponse
import com.teamhide.kream.common.security.CurrentUser
import com.teamhide.kream.product.domain.usecase.ImmediatePurchaseUseCase
import com.teamhide.kream.product.domain.usecase.ImmediateSaleUseCase
import com.teamhide.kream.product.ui.api.dto.ImmediatePurchaseRequest
import com.teamhide.kream.product.ui.api.dto.ImmediatePurchaseResponse
import com.teamhide.kream.product.ui.api.dto.ImmediateSaleRequest
import com.teamhide.kream.product.ui.api.dto.ImmediateSaleResponse
import jakarta.validation.Valid
import org.springframework.http.HttpStatus
import org.springframework.security.core.annotation.AuthenticationPrincipal
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/v1/bid")
class ImmediateBidV1Controller(
    private val immediatePurchaseUseCase: ImmediatePurchaseUseCase,
    private val immediateSaleUseCase: ImmediateSaleUseCase,
) {
    @PostMapping("/purchase")
    fun immediatePurchase(
        @AuthenticationPrincipal currentUser: CurrentUser,
        @RequestBody @Valid body: ImmediatePurchaseRequest
    ): ApiResponse<ImmediatePurchaseResponse> {
        val command = body.toCommand(userId = currentUser.id)
        val purchaseResult = immediatePurchaseUseCase.execute(command = command)
        val response = ImmediatePurchaseResponse.from(responseDto = purchaseResult)
        return ApiResponse.success(body = response, statusCode = HttpStatus.OK)
    }

    @PostMapping("/sale")
    fun immediateSale(
        @AuthenticationPrincipal currentUser: CurrentUser,
        @RequestBody @Valid body: ImmediateSaleRequest,
    ): ApiResponse<ImmediateSaleResponse> {
        val command = body.toCommand(userId = currentUser.id)
        val saleResponse = immediateSaleUseCase.execute(command = command)
        val response = ImmediateSaleResponse.from(responseDto = saleResponse)
        return ApiResponse.success(body = response, statusCode = HttpStatus.OK)
    }
}
