package com.teamhide.kream.location.adapter.`in`.rest.v1

import com.teamhide.kream.common.response.ApiResponse
import com.teamhide.kream.common.security.CurrentUser
import com.teamhide.kream.location.application.port.`in`.GetLocationsQuery
import com.teamhide.kream.location.application.port.`in`.GetLocationsUseCase
import org.springframework.http.HttpStatus
import org.springframework.security.core.annotation.AuthenticationPrincipal
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import java.time.LocalDateTime

data class GetLocationsResponse(val locations: List<Location>) {
    data class Location(
        val userId: Long,
        val nickname: String,
        val lat: Double,
        val lng: Double,
        val stayedAt: LocalDateTime
    )
}

@RestController
@RequestMapping("/api/v1/locations")
class GetLocationsV1Controller(
    private val useCase: GetLocationsUseCase,
) {
    @GetMapping("")
    fun getLocations(@AuthenticationPrincipal currentUser: CurrentUser): ApiResponse<GetLocationsResponse> {
        val query = GetLocationsQuery(userId = currentUser.id)
        val locations = useCase.execute(query = query).map {
            GetLocationsResponse.Location(
                userId = it.userId,
                nickname = it.nickname,
                lat = it.location.lat,
                lng = it.location.lng,
                stayedAt = it.stayedAt,
            )
        }
        val response = GetLocationsResponse(locations = locations)
        return ApiResponse.success(body = response, statusCode = HttpStatus.OK)
    }
}
