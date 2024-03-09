package com.teamhide.kream.product.ui.api

import com.teamhide.kream.common.response.ApiResponse
import com.teamhide.kream.common.security.CurrentUser
import com.teamhide.kream.product.domain.usecase.ImmediateSaleCommand
import com.teamhide.kream.product.domain.usecase.ImmediateSaleUseCase
import jakarta.validation.Valid
import jakarta.validation.constraints.NotNull
import org.springframework.http.HttpStatus
import org.springframework.security.core.annotation.AuthenticationPrincipal
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

data class ImmediateSaleRequest(
    @field:NotNull
    val biddingId: Long,
)

data class ImmediateSaleResponse(
    val biddingId: Long,
    val price: Int,
)

@RestController
@RequestMapping("/v1/bid")
class ImmediateSaleV1Controller(
    private val immediateSaleUseCase: ImmediateSaleUseCase,
) {

    @PostMapping("/sale")
    fun immediateSale(
        @AuthenticationPrincipal currentUser: CurrentUser,
        @RequestBody @Valid body: ImmediateSaleRequest,
    ): ApiResponse<ImmediateSaleResponse> {
        val command = ImmediateSaleCommand(biddingId = body.biddingId, userId = currentUser.id)
        val response = immediateSaleUseCase.execute(command = command).let {
            ImmediateSaleResponse(
                biddingId = it.biddingId,
                price = it.price,
            )
        }
        return ApiResponse.success(body = response, statusCode = HttpStatus.OK)
    }
}
