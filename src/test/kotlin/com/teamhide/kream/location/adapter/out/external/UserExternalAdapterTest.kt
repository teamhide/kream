package com.teamhide.kream.location.adapter.out.external

import com.teamhide.kream.location.domain.vo.UserLocation
import com.teamhide.kream.user.application.port.`in`.GetFriendLocationsUseCase
import com.teamhide.kream.user.application.port.`in`.GetUserLocationUseCase
import com.teamhide.kream.user.application.port.`in`.GetUserUseCase
import com.teamhide.kream.user.application.port.`in`.IsFriendWithUseCase
import com.teamhide.kream.user.application.port.`in`.UpdateUserLocationUseCase
import com.teamhide.kream.user.domain.model.UserWithLocation
import com.teamhide.kream.user.domain.vo.Location
import com.teamhide.kream.user.makeUser
import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.shouldBe
import io.mockk.every
import io.mockk.mockk
import java.time.LocalDateTime

internal class UserExternalAdapterTest : StringSpec({
    val getFriendLocationsUseCase = mockk<GetFriendLocationsUseCase>()
    val updateUserLocationUseCase = mockk<UpdateUserLocationUseCase>()
    val getUserLocationUseCase = mockk<GetUserLocationUseCase>()
    val getUserUseCase = mockk<GetUserUseCase>()
    val isFriendWithUseCase = mockk<IsFriendWithUseCase>()
    val externalAdapter = UserExternalAdapter(
        getFriendLocationsUseCase = getFriendLocationsUseCase,
        updateUserLocationUseCase = updateUserLocationUseCase,
        getUserLocationUseCase = getUserLocationUseCase,
        getUserUseCase = getUserUseCase,
        isFriendWithUseCase = isFriendWithUseCase,
    )

    "특정 유저의 친구들 위치를 조회한다" {
        // Given
        val userId = 1L
        val userWithLocation = UserWithLocation(
            userId = 1L, nickname = "hide", location = Location(lat = 37.123, lng = 127.123), stayedAt = LocalDateTime.now()
        )
        every { getFriendLocationsUseCase.execute(any()) } returns arrayListOf(userWithLocation)

        // When
        val sut = externalAdapter.getFriendLocations(userId = userId)

        // Then
        sut.size shouldBe 1
        val location = sut[0]
        location.userId shouldBe userWithLocation.userId
        location.nickname shouldBe userWithLocation.nickname
        location.location shouldBe userWithLocation.location
        location.stayedAt shouldBe userWithLocation.stayedAt
    }

    "id를 기반으로 유저의 위치를 업데이트한다" {
        // Given
        val userId = 1L
        val lat = 10.1234
        val lng = 10.1211
        every { updateUserLocationUseCase.execute(any()) } returns 1L

        // When
        val count = externalAdapter.updateUserLocation(userId = userId, location = UserLocation(lat = lat, lng = lng))

        // Then
        count shouldBe 1
    }

    "id에 해당하는 유저의 위치를 조회한다" {
        // Given
        val userId = 1L
        val userWithLocation = UserWithLocation(
            userId = 1L, nickname = "hide", location = Location(lat = 37.123, lng = 127.123), stayedAt = LocalDateTime.now()
        )
        every { getUserLocationUseCase.execute(any()) } returns userWithLocation

        // When
        val sut = externalAdapter.getUserLocation(userId = userId)

        // Then
        sut.userId shouldBe userWithLocation.userId
        sut.nickname shouldBe userWithLocation.nickname
        sut.location shouldBe userWithLocation.location
        sut.stayedAt shouldBe userWithLocation.stayedAt
    }

    "id로 유저를 조회한다" {
        // Given
        val userId = 1L
        val user = makeUser()
        every { getUserUseCase.execute(any()) } returns user

        // When
        val sut = externalAdapter.getUser(userId = userId)

        // Then
        sut.id shouldBe user.id
        sut.password shouldBe user.password
        sut.email shouldBe user.email
        sut.nickname shouldBe user.nickname
        sut.status shouldBe user.status
        sut.location.lat shouldBe user.location.lat
        sut.location.lng shouldBe user.location.lng
        sut.stayedAt shouldBe user.stayedAt
    }

    "특정 유저가 특정 유저를 친구로 등록했는지 확인한다" {
        // Given
        val userId = 1L
        val friendUserId = 2L
        every { isFriendWithUseCase.execute(any()) } returns false

        // When
        val sut = externalAdapter.isFriendWith(userId = userId, friendUserId = friendUserId)

        // Then
        sut shouldBe false
    }
})
