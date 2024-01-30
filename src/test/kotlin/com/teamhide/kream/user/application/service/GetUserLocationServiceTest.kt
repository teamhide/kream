package com.teamhide.kream.user.application.service

import com.teamhide.kream.user.application.exception.UserNotFoundException
import com.teamhide.kream.user.application.port.`in`.GetUserLocationQuery
import com.teamhide.kream.user.application.port.out.GetUserPersistencePort
import com.teamhide.kream.user.makeUser
import io.kotest.assertions.throwables.shouldThrow
import io.kotest.core.spec.style.BehaviorSpec
import io.kotest.matchers.shouldBe
import io.mockk.every
import io.mockk.mockk

class GetUserLocationServiceTest : BehaviorSpec({
    val getUserPersistencePort = mockk<GetUserPersistencePort>()
    val getUserLocationService = GetUserLocationService(getUserPersistencePort = getUserPersistencePort)

    Given("없는 유저를 대상으로") {
        every { getUserPersistencePort.findById(any()) } returns null
        val query = GetUserLocationQuery(userId = 1L)

        When("위치를 조회하면") {
            Then("예외가 발생한다") {
                shouldThrow<UserNotFoundException> { getUserLocationService.execute(query) }
            }
        }
    }

    Given("1번 유저가 존재할 때") {
        val user = makeUser(id = 1L)
        every { getUserPersistencePort.findById(any()) } returns user
        val query = GetUserLocationQuery(userId = user.id)

        When("위치를 조회하면") {
            val sut = getUserLocationService.execute(query)

            Then("위치 정보가 리턴된다") {
                sut.userId shouldBe user.id
                sut.location.lat shouldBe user.location.lat
                sut.location.lng shouldBe user.location.lng
                sut.stayedAt shouldBe user.stayedAt
            }
        }
    }
})
