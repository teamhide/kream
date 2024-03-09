package com.teamhide.kream.product.ui.api

import com.teamhide.kream.common.response.ApiResponse
import com.teamhide.kream.common.security.CurrentUser
import com.teamhide.kream.product.domain.usecase.BidCommand
import com.teamhide.kream.product.domain.usecase.BidUseCase
import com.teamhide.kream.product.domain.vo.BiddingType
import jakarta.validation.Valid
import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.NotNull
import org.springframework.http.HttpStatus
import org.springframework.security.core.annotation.AuthenticationPrincipal
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

data class BidRequest(
    @field:NotNull
    val productId: Long,

    @field:NotNull
    val price: Int,

    @field:NotBlank
    val size: String,

    @field:NotNull
    val biddingType: BiddingType,
)

data class BidResponse(
    val biddingId: Long,
    val price: Int,
    val size: String,
    val biddingType: BiddingType,
)

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
        val command = body.let {
            BidCommand(
                productId = it.productId,
                price = it.price,
                size = it.size,
                biddingType = it.biddingType,
                userId = currentUser.id,
            )
        }
        val response = bidUseCase.execute(command = command).let {
            BidResponse(
                biddingId = it.biddingId,
                price = it.price,
                size = it.size,
                biddingType = it.biddingType,
            )
        }
        return ApiResponse.success(body = response, statusCode = HttpStatus.OK)
    }
}
