package com.teamhide.kream.product.ui.api

import com.teamhide.kream.common.response.ApiResponse
import com.teamhide.kream.common.security.CurrentUser
import com.teamhide.kream.product.domain.usecase.BidUseCase
import com.teamhide.kream.product.ui.api.dto.BidRequest
import com.teamhide.kream.product.ui.api.dto.BidResponse
import jakarta.validation.Valid
import org.springframework.http.HttpStatus
import org.springframework.security.core.annotation.AuthenticationPrincipal
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/v1/bid")
class BidV1Controller(
    private val bidUseCase: BidUseCase,
) {
    @PostMapping("")
    fun bid(
        @AuthenticationPrincipal currentUser: CurrentUser,
        @RequestBody @Valid body: BidRequest
    ): ApiResponse<BidResponse> {
        val command = body.toCommand(userId = currentUser.id)
        val bidResponseDto = bidUseCase.execute(command = command)
        val response = BidResponse.from(bidResponseDto = bidResponseDto)
        return ApiResponse.success(body = response, statusCode = HttpStatus.OK)
    }
}
