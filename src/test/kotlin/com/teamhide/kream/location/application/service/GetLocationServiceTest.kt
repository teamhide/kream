package com.teamhide.kream.location.application.service

import com.teamhide.kream.location.application.exception.GhostModeUserException
import com.teamhide.kream.location.application.exception.UserIsNotFriendException
import com.teamhide.kream.location.application.port.`in`.GetLocationQuery
import com.teamhide.kream.location.application.port.out.UserExternalPort
import com.teamhide.kream.user.domain.vo.UserStatus
import com.teamhide.kream.user.makeUser
import io.kotest.assertions.throwables.shouldThrow
import io.kotest.core.spec.style.BehaviorSpec
import io.kotest.matchers.shouldBe
import io.mockk.every
import io.mockk.mockk

class GetLocationServiceTest : BehaviorSpec({
    val userExternalPort = mockk<UserExternalPort>()
    val getLocationService = GetLocationService(userExternalPort = userExternalPort)

    Given("친구가 아닌 유저의") {
        val query = GetLocationQuery(userId = 1L, friendUserId = 2L)
        every { userExternalPort.isFriendWith(any(), any()) } returns false

        When("위치 조회를 요청하면") {
            Then("예외가 발생한다") {
                shouldThrow<UserIsNotFriendException> { getLocationService.execute(query = query) }
            }
        }
    }

    Given("유령 모드 상태의 유저의") {
        val query = GetLocationQuery(userId = 1L, friendUserId = 2L)
        every { userExternalPort.isFriendWith(any(), any()) } returns true
        val user = makeUser(status = UserStatus.GHOST)
        every { userExternalPort.getUser(any()) } returns user

        When("위치 조회를 요청하면") {
            Then("예외가 발생한다") {
                shouldThrow<GhostModeUserException> { getLocationService.execute(query = query) }
            }
        }
    }

    Given("유저 어그리거트에게") {
        every { userExternalPort.isFriendWith(any(), any()) } returns true
        val user = makeUser(status = UserStatus.ONLINE)
        every { userExternalPort.getUser(any()) } returns user
        val query = GetLocationQuery(userId = 1L, friendUserId = 2L)

        When("위치를 조회하면") {
            val sut = getLocationService.execute(query = query)

            Then("유저의 위치가 리턴된다") {
                sut.userId shouldBe user.id
                sut.nickname shouldBe user.nickname
                sut.location shouldBe user.location
                sut.stayedAt shouldBe user.stayedAt
            }
        }
    }
})
