package com.teamhide.kream.bidding.adapter.`in`.api.v1

import com.teamhide.kream.bidding.domain.usecase.ImmediatePurchaseCommand
import com.teamhide.kream.bidding.domain.usecase.ImmediatePurchaseUseCase
import com.teamhide.kream.common.response.ApiResponse
import com.teamhide.kream.common.security.CurrentUser
import jakarta.validation.Valid
import jakarta.validation.constraints.NotNull
import org.springframework.http.HttpStatus
import org.springframework.security.core.annotation.AuthenticationPrincipal
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

data class ImmediatePurchaseRequest(
    @field:NotNull
    val biddingId: Long,
)

data class ImmediatePurchaseResponse(
    val biddingId: Long,
    val price: Int,
)

@RestController
@RequestMapping("/v1/bid")
class ImmediatePurchaseV1Controller(
    private val immediatePurchaseUseCase: ImmediatePurchaseUseCase,
) {
    @PostMapping("/purchase")
    fun immediatePurchase(
        @AuthenticationPrincipal currentUser: CurrentUser,
        @RequestBody @Valid body: ImmediatePurchaseRequest
    ): ApiResponse<ImmediatePurchaseResponse> {
        val command = ImmediatePurchaseCommand(biddingId = body.biddingId, userId = currentUser.id)
        val response = immediatePurchaseUseCase.execute(command = command).let {
            ImmediatePurchaseResponse(
                biddingId = it.biddingId,
                price = it.price,
            )
        }
        return ApiResponse.success(body = response, statusCode = HttpStatus.OK)
    }
}
