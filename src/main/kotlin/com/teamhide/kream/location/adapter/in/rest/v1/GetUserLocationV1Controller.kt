package com.teamhide.kream.location.adapter.`in`.rest.v1

import com.teamhide.kream.common.response.ApiResponse
import com.teamhide.kream.common.security.CurrentUser
import com.teamhide.kream.location.application.port.`in`.GetLocationQuery
import com.teamhide.kream.location.application.port.`in`.GetLocationUseCase
import com.teamhide.kream.user.domain.vo.Location
import org.springframework.http.HttpStatus
import org.springframework.security.core.annotation.AuthenticationPrincipal
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import java.time.LocalDateTime

data class GetUserLocationResponse(
    val userId: Long,
    val nickname: String,
    val location: Location,
    val stayedAt: LocalDateTime,
)

@RestController
@RequestMapping("/api/v1/locations")
class GetUserLocationV1Controller(
    private val useCase: GetLocationUseCase,
) {
    @GetMapping("/{userId}")
    fun getUserLocation(
        @AuthenticationPrincipal currentUser: CurrentUser,
        @PathVariable("userId") userId: Long,
    ): ApiResponse<GetUserLocationResponse> {
        val query = GetLocationQuery(userId = currentUser.id, friendUserId = userId)
        val location = useCase.execute(query = query)
        val response = GetUserLocationResponse(
            userId = location.userId,
            nickname = location.nickname,
            location = location.location,
            stayedAt = location.stayedAt,
        )
        return ApiResponse.success(body = response, statusCode = HttpStatus.OK)
    }
}
