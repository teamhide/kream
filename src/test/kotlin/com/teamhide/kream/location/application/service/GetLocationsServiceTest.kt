package com.teamhide.kream.location.application.service

import com.teamhide.kream.location.application.port.`in`.GetLocationsQuery
import com.teamhide.kream.location.application.port.out.UserExternalPort
import com.teamhide.kream.user.domain.model.UserWithLocation
import com.teamhide.kream.user.domain.vo.Location
import io.kotest.core.spec.style.BehaviorSpec
import io.kotest.matchers.shouldBe
import io.mockk.every
import io.mockk.mockk
import java.time.LocalDateTime

class GetLocationsServiceTest : BehaviorSpec({
    val userExternalPort = mockk<UserExternalPort>()
    val getLocationsService = GetLocationsService(userExternalPort = userExternalPort)

    Given("특정 유저의 아이디를 통해") {
        val userId = 1L
        val query = GetLocationsQuery(userId = userId)
        val userWithLocation = UserWithLocation(
            userId = userId, nickname = "hide", location = Location(lat = 37.123, lng = 127.123), stayedAt = LocalDateTime.now()
        )
        every { userExternalPort.getFriendLocations(userId) } returns arrayListOf(userWithLocation)

        When("친구들의 위치를 조회하면") {
            val sut = getLocationsService.execute(query = query)

            Then("대상 친구들의 위치가 리턴된다") {
                sut.size shouldBe 1
                val location = sut[0]
                location.userId shouldBe userWithLocation.userId
                location.nickname shouldBe userWithLocation.nickname
                location.location shouldBe userWithLocation.location
                location.stayedAt shouldBe userWithLocation.stayedAt
            }
        }
    }
})
