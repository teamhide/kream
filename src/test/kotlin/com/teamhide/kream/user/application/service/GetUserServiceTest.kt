package com.teamhide.kream.user.application.service

import com.teamhide.kream.user.application.exception.UserNotFoundException
import com.teamhide.kream.user.application.port.`in`.GetUserQuery
import com.teamhide.kream.user.application.port.out.GetUserPersistencePort
import com.teamhide.kream.user.makeUser
import io.kotest.assertions.throwables.shouldThrow
import io.kotest.core.spec.style.BehaviorSpec
import io.kotest.matchers.shouldBe
import io.mockk.every
import io.mockk.mockk

class GetUserServiceTest : BehaviorSpec({
    val getUserPersistencePort = mockk<GetUserPersistencePort>()
    val getUserService = GetUserService(getUserPersistencePort = getUserPersistencePort)

    Given("유저가 존재하지 않을 때") {
        val query = GetUserQuery(userId = 1L)
        every { getUserPersistencePort.findById(any()) } returns null

        When("유저 조회를 요청하면") {
            Then("예외가 발생한다") {
                shouldThrow<UserNotFoundException> { getUserService.execute(query = query) }
            }
        }
    }

    Given("유저가 존재할 때") {
        val query = GetUserQuery(userId = 1L)
        val user = makeUser()
        every { getUserPersistencePort.findById(any()) } returns user

        When("유저 조회를 요청하면") {
            val sut = getUserService.execute(query = query)

            Then("유저 정보가 리턴된다") {
                sut.id shouldBe user.id
                sut.password shouldBe user.password
                sut.email shouldBe user.email
                sut.nickname shouldBe user.nickname
                sut.status shouldBe user.status
                sut.location.lat shouldBe user.location.lat
                sut.location.lng shouldBe user.location.lng
                sut.stayedAt shouldBe user.stayedAt
            }
        }
    }
})
